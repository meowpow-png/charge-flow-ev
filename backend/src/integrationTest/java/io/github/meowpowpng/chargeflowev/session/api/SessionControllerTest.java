package io.github.meowpowpng.chargeflowev.session.api;

import com.jayway.jsonpath.JsonPath;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SessionControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should create a new session when no active session exists")
    void should_CreateSession_when_NoActiveSessionExists() throws Exception {
        mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should reject session creation when an active session already exists")
    void should_RejectSessionCreation_when_ActiveSessionAlreadyExists() throws Exception {
        // first session (active)
        mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk());

        // second attempt
        mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return all sessions when sessions exist")
    void should_ReturnAllSessions_when_SessionsExist() throws Exception {
        // create first session
        var first = mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andReturn();

        // finalize first session
        mockMvc.perform(postFinalizeSession(readIdFromResult(first)))
                .andExpect(status().isOk());

        // create second session
        var second = mockMvc.perform(postCreateSession(SessionType.CHARGING))
                .andExpect(status().isOk())
                .andReturn();

        // finalize second session
        mockMvc.perform(postFinalizeSession(readIdFromResult(second)))
                .andExpect(status().isOk());

        // verify that sessions exist
        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return session by id when id exists")
    void should_ReturnSessionById_when_IdExists() throws Exception {
        var created = mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andReturn();

        String id = readIdFromResult(created);
        mockMvc.perform(get("/sessions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("Should return not found when session id does not exist")
    void should_ReturnNotFound_when_SessionIdDoesNotExist() throws Exception {
        String unknownId = UUID.randomUUID().toString();

        mockMvc.perform(get("/sessions/" + unknownId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should finalize session when it is active")
    void should_FinalizeSession_when_SessionIsActive() throws Exception {
        var created = mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andReturn();

        String id = readIdFromResult(created);
        mockMvc.perform(postFinalizeSession(id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("FINALIZED"))
                .andExpect(jsonPath("$.endedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Should reject finalization when session is already finalized")
    void should_RejectFinalization_when_SessionIsAlreadyFinalized() throws Exception {
        var created = mockMvc.perform(postCreateSession(SessionType.RIDE))
                .andExpect(status().isOk())
                .andReturn();

        String id = readIdFromResult(created);

        mockMvc.perform(postFinalizeSession(id))
                .andExpect(status().isOk());

        mockMvc.perform(postFinalizeSession(id))
                .andExpect(status().isConflict());
    }

    private static MockHttpServletRequestBuilder postCreateSession(SessionType type) {
        return post("/sessions").param("type", type.toString());
    }

    private static MockHttpServletRequestBuilder postFinalizeSession(String id) {
        return post("/sessions/" + id + "/finalize");
    }

    private static String readIdFromResult(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
