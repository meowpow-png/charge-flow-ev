package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class FinalizedSessionImpl implements FinalizedSession {

    private final Session session;

    FinalizedSessionImpl(Session session) {
        this.session = Objects.requireNonNull(session, "session must not be null");
    }

    @Override
    public UUID getId() {
        return session.getId();
    }

    @Override
    public SessionType getType() {
        return session.getType();
    }

    @Override
    public Instant getStartedAt() {
        return session.getStartedAt();
    }

    @Override
    public Instant getEndedAt() {
        return Objects.requireNonNull(session.getEndedAt());
    }

    @Override
    public BigDecimal getEnergyTotal() {
        return session.getEnergyTotal();
    }
}
