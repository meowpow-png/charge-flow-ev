package io.github.meowpowpng.chargeflowev.telemetry.api;

import java.util.Objects;
import java.util.UUID;

public record TelemetryResponse(UUID telemetryId) {

    public TelemetryResponse {
        Objects.requireNonNull(telemetryId, "telemetryId must not be null");
    }
}
