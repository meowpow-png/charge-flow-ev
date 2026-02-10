package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionState;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository repository;

    public SessionService(SessionRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    public Session createSession(SessionType type) {
        if (repository.existsByState(SessionState.ACTIVE)) {
            throw new IllegalStateException("An active session already exists");
        }
        var uuid = UUID.randomUUID();
        var now = Instant.now();

        Session session = new Session(uuid, type, now, SessionState.ACTIVE);
        return repository.save(session);
    }

    public List<Session> getAllSessions() {
        return repository.findAll();
    }

    public Session getSession(UUID id) {
        // TODO: Map missing session to HTTP 404 instead of default 500
        return repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Session not found")
        );
    }

    public Session finalizeSession(UUID id) {
        Session session = getSession(id);
        if (session.isFinalized()) {
            throw new IllegalStateException("Session already finalized");
        }
        session.finalizeSession(Clock.systemUTC());
        return repository.save(session);
    }
}
