package io.github.meowpowpng.chargeflowev.session.api;

import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import java.math.BigDecimal;
import java.util.UUID;

public interface SessionCommand {

    UUID startSession(SessionType type);

    void finalizeSession(UUID sessionId);

    void addEnergy(UUID sessionId, BigDecimal delta);
}
