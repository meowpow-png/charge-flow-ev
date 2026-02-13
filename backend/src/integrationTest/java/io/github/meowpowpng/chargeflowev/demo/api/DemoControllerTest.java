package io.github.meowpowpng.chargeflowev.demo.api;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NullUnmarked
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class DemoControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should run demo successfully when request is valid")
    void should_RunDemoSuccessfully_when_RequestIsValid() throws Exception {
        mockMvc.perform(postRunDemo(3, 2))
                .andExpect(status().isOk())
                // session IDs
                .andExpect(jsonPath("$.rideSessionId").exists())
                .andExpect(jsonPath("$.chargingSessionId").exists())
                // ride billing
                .andExpect(jsonPath("$.rideBilling").exists())
                .andExpect(jsonPath("$.rideBilling.sessionId").exists())
                .andExpect(jsonPath("$.rideBilling.billableEnergy").exists())
                .andExpect(jsonPath("$.rideBilling.unitPrice").exists())
                .andExpect(jsonPath("$.rideBilling.totalCost").exists())
                // charging billing
                .andExpect(jsonPath("$.chargingBilling").exists())
                .andExpect(jsonPath("$.chargingBilling.sessionId").exists())
                .andExpect(jsonPath("$.chargingBilling.billableEnergy").exists())
                .andExpect(jsonPath("$.chargingBilling.unitPrice").exists())
                .andExpect(jsonPath("$.chargingBilling.totalCost").exists());
    }

    @Test
    @DisplayName("Should return bad request when rideSamples is less than one")
    void should_ReturnBadRequest_when_RideSamplesIsLessThanOne() throws Exception {
        mockMvc.perform(postRunDemo(0, 2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when chargingSamples is less than one")
    void should_ReturnBadRequest_when_ChargingSamplesIsLessThanOne() throws Exception {
        mockMvc.perform(postRunDemo(2, 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return conflict when active session already exists")
    void should_ReturnConflict_when_ActiveSessionAlreadyExists() throws Exception {
        mockMvc.perform(postCreateRideSession())
                .andExpect(status().isOk());

        mockMvc.perform(postRunDemo(2, 2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").exists());
    }

    private static MockHttpServletRequestBuilder postRunDemo(int rideSamples, int chargingSamples) {
        return post("/demo/run")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rideSamples\": %d, \"chargingSamples\": %d}"
                        .formatted(rideSamples, chargingSamples));
    }

    private static MockHttpServletRequestBuilder postCreateRideSession() {
        return post("/sessions").param("type", SessionType.RIDE.toString());
    }
}
