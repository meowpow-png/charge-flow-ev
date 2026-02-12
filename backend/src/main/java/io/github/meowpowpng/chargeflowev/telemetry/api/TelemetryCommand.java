package io.github.meowpowpng.chargeflowev.telemetry.api;

import java.math.BigDecimal;
import java.util.UUID;

public interface TelemetryCommand {

    void recordTelemetry(UUID sessionId, BigDecimal delta);
}
