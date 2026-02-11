package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.api.SessionCommand;
import io.github.meowpowpng.chargeflowev.session.domain.Session;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class SessionCommandImpl implements SessionCommand {

    private final SessionRepository repository;

    public SessionCommandImpl(SessionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    @Override
    public void addEnergy(UUID sessionId, BigDecimal delta) {
        Session session = repository.findById(sessionId).filter(Session::isActive).orElseThrow(() ->
                new IllegalArgumentException("Active session not found (id=" + sessionId + ')')
        );
        session.addEnergy(delta);
        repository.save(session);
    }
}
