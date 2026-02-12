package io.github.meowpowpng.chargeflowev.telemetry.api;

import io.github.meowpowpng.chargeflowev.telemetry.internal.TelemetryService;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/sessions/{sessionId}/telemetry")
public final class TelemetryController {

    private final TelemetryService service;

    TelemetryController(TelemetryService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @PostMapping
    public TelemetryResponse recordTelemetry(
            @PathVariable UUID sessionId,
            @RequestBody TelemetryRequest request
    ) {
        var telemetry = service.recordTelemetryInternal(sessionId, request.energyDelta());
        return new TelemetryResponse(telemetry.getId());
    }
}
