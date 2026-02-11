package io.github.meowpowpng.chargeflowev.telemetry.api;

import java.math.BigDecimal;
import java.util.Objects;

public record TelemetryRequest(BigDecimal energyDelta) {

    public TelemetryRequest {
        Objects.requireNonNull(energyDelta, "energyDelta must not be null");
    }
}
