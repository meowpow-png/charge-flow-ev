package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionState;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        Objects.requireNonNull(sessionRepository, "repository must not be null");
        this.sessionRepository = sessionRepository;
    }

    @PostMapping
    public Session createSession(@RequestParam SessionType type) {
        var uuid = UUID.randomUUID();
        var now = Instant.now();

        Session session = new Session(uuid, type, now, SessionState.ACTIVE);
        return sessionRepository.save(session);
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable UUID id) {
        // TODO: Map missing session to HTTP 404 instead of default 500
        return sessionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Session not found")
        );
    }
}
