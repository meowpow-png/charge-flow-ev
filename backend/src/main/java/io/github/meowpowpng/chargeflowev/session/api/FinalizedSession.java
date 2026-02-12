package io.github.meowpowpng.chargeflowev.session.api;

import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface FinalizedSession {

    UUID getId();

    SessionType getType();

    Instant getStartedAt();

    Instant getEndedAt();

    BigDecimal getEnergyTotal();
}
