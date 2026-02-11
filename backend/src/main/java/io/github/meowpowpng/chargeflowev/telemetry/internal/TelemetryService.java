package io.github.meowpowpng.chargeflowev.telemetry.internal;

import io.github.meowpowpng.chargeflowev.session.api.SessionCommand;
import io.github.meowpowpng.chargeflowev.telemetry.domain.Telemetry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class TelemetryService {

    private final TelemetryRepository repository;
    private final SessionCommand session;

    public TelemetryService(TelemetryRepository repository, SessionCommand session) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.session = Objects.requireNonNull(session, "session must not be null");
    }

    @Transactional
    public Telemetry recordTelemetry(UUID sessionId, BigDecimal delta) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Telemetry telemetry = new Telemetry(id, sessionId, delta, now);
        repository.save(telemetry);

        session.addEnergy(sessionId, delta);
        return telemetry;
    }
}
