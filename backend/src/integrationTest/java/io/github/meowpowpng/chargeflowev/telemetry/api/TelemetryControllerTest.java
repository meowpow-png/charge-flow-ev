package io.github.meowpowpng.chargeflowev.telemetry.api;

import com.jayway.jsonpath.JsonPath;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NullUnmarked
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class TelemetryControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should record telemetry when session is active")
    void should_RecordTelemetry_when_SessionIsActive() throws Exception {
        var created = mockMvc.perform(postCreateRideSession())
                .andExpect(status().isOk())
                .andReturn();

        String id = readIdFromResult(created);
        mockMvc.perform(postTelemetry(id, new BigDecimal("1.25")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telemetryId").exists());
    }

    @Test
    @DisplayName("Should reject telemetry when session is finalized")
    void should_RejectTelemetry_when_SessionIsFinalized() throws Exception {
        var created = mockMvc.perform(postCreateRideSession())
                .andExpect(status().isOk())
                .andReturn();

        String id = readIdFromResult(created);

        mockMvc.perform(postFinalizeSession(id))
                .andExpect(status().isOk());

        mockMvc.perform(postTelemetry(id, new BigDecimal("1.00")))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return not found when session does not exist")
    void should_ReturnNotFound_when_SessionDoesNotExist() throws Exception {
        String unknownId = UUID.randomUUID().toString();

        mockMvc.perform(postTelemetry(unknownId, new BigDecimal("1.00")))
                .andExpect(status().isNotFound());
    }

    private static MockHttpServletRequestBuilder postCreateRideSession() {
        return post("/sessions").param("type", SessionType.RIDE.toString());
    }

    private static MockHttpServletRequestBuilder postFinalizeSession(String id) {
        return post("/sessions/" + id + "/finalize");
    }

    private static MockHttpServletRequestBuilder postTelemetry(String sessionId, BigDecimal delta) {
        return post("/sessions/" + sessionId + "/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"energyDelta\": %s}".formatted(delta));
    }

    private static String readIdFromResult(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
