package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionCommand;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionState;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

@Service
public class SessionService implements SessionCommand, SessionQuery {

    private final SessionRepository repository;

    public SessionService(SessionRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    @Override
    public UUID startSession(SessionType type) {
        return createSession(type).getId();
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

    @Override
    public FinalizedSession finalizeSession(UUID sessionId) {
        return new FinalizedSessionImpl(finalizeAndSave(sessionId));
    }

    public Session finalizeAndSave(UUID id) {
        Session session = getSession(id);
        if (session.isFinalized()) {
            throw new IllegalStateException("Session already finalized");
        }
        session.finalizeSession(Clock.systemUTC());
        return repository.save(session);
    }

    public void addEnergy(UUID sessionId, BigDecimal delta) {
        Session session = repository.findById(sessionId).filter(Session::isActive).orElseThrow(() ->
                new IllegalArgumentException("Active session not found (id=" + sessionId + ')')
        );
        session.addEnergy(delta);
        repository.save(session);
    }

    @Override
    public Optional<FinalizedSession> findFinalizedById(UUID sessionId) {
        var session = repository.findById(sessionId).filter(Session::isFinalized);
        return session.map(FinalizedSessionImpl::new);
    }

    @Override
    public List<FinalizedSession> findAllFinalized() {
        var result = repository.findAll().stream()
                .filter(Session::isFinalized)
                .map(FinalizedSessionImpl::new)
                .toList();

        return new ArrayList<>(result);
    }

    @Override
    public boolean hasActiveSession() {
        return repository.existsByState(SessionState.ACTIVE);
    }
}
