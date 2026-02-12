package io.github.meowpowpng.chargeflowev.analytics.api;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NullUnmarked
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AnalyticsControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return zero summary when no finalized sessions exist")
    void should_ReturnZeroSummary_when_NoFinalizedSessionsExist() throws Exception {
        mockMvc.perform(getSummary())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRideEnergy").value(0))
                .andExpect(jsonPath("$.totalChargingEnergy").value(0))
                .andExpect(jsonPath("$.totalRevenue").value(0))
                .andExpect(jsonPath("$.totalCost").value(0))
                .andExpect(jsonPath("$.netProfit").value(0))
                .andExpect(jsonPath("$.rideCount").value(0))
                .andExpect(jsonPath("$.chargingCount").value(0));
    }

    @Test
    @DisplayName("Should return aggregated summary when finalized sessions exist")
    void should_ReturnAggregatedSummary_when_FinalizedSessionsExist() throws Exception {
        // create and finalize RIDE session
        var rideCreated = mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andReturn();

        String rideId = readIdFromResult(rideCreated);

        mockMvc.perform(postTelemetry(rideId, new BigDecimal("2.00")))
                .andExpect(status().isOk());

        mockMvc.perform(postFinalizeSession(rideId))
                .andExpect(status().isOk());

        // create and finalize CHARGING session
        var chargingCreated = mockMvc.perform(postCreateSession(SessionType.CHARGING))
                .andExpect(status().isOk())
                .andReturn();

        String chargingId = readIdFromResult(chargingCreated);

        mockMvc.perform(postTelemetry(chargingId, new BigDecimal("1.50")))
                .andExpect(status().isOk());

        mockMvc.perform(postFinalizeSession(chargingId))
                .andExpect(status().isOk());

        mockMvc.perform(getSummary())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRideEnergy").value(2.00))
                .andExpect(jsonPath("$.totalChargingEnergy").value(1.50))
                .andExpect(jsonPath("$.rideCount").value(1))
                .andExpect(jsonPath("$.chargingCount").value(1))
                .andExpect(jsonPath("$.totalRevenue").exists())
                .andExpect(jsonPath("$.totalCost").exists())
                .andExpect(jsonPath("$.netProfit").exists());
    }

    private static MockHttpServletRequestBuilder postCreateSession(SessionType type) {
        return post("/sessions").param("type", type.toString());
    }

    private static MockHttpServletRequestBuilder postFinalizeSession(String id) {
        return post("/sessions/" + id + "/finalize");
    }

    private static MockHttpServletRequestBuilder postTelemetry(String sessionId, BigDecimal delta) {
        return post("/sessions/" + sessionId + "/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"energyDelta\": %s}".formatted(delta));
    }

    private static MockHttpServletRequestBuilder getSummary() {
        return get("/analytics/summary");
    }

    private static String readIdFromResult(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
