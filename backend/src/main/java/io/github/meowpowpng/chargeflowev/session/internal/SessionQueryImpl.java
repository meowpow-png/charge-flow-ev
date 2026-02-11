package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.Session;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionQueryImpl implements SessionQuery {

    private final SessionRepository repository;

    public SessionQueryImpl(SessionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    @Override
    public Optional<FinalizedSession> findFinalizedById(UUID sessionId) {
        var session = repository.findById(sessionId).filter(Session::isFinalized);
        return session.map(FinalizedSessionImpl::new);
    }
}
