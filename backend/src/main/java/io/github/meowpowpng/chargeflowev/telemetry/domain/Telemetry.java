package io.github.meowpowpng.chargeflowev.telemetry.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "telemetry")
@SuppressWarnings("NotNullFieldNotInitialized")
public class Telemetry {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID sessionId;

    @Column(nullable = false, precision = 10, scale = 3, updatable = false)
    private BigDecimal energyDelta;

    @Column(nullable = false, updatable = false)
    private Instant emittedAt;

    @SuppressWarnings("unused")
    Telemetry() {}

    public Telemetry(UUID id, UUID sessionId, BigDecimal energyDelta, Instant emittedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId must not be null");

        Objects.requireNonNull(energyDelta, "energyDelta must not be null");
        BigDecimal normalized = energyDelta.setScale(3, RoundingMode.HALF_UP);
        if (energyDelta.signum() < 0) {
            throw new IllegalArgumentException("energyDelta must not be a negative value");
        }
        this.energyDelta = normalized;
        this.emittedAt = Objects.requireNonNull(emittedAt, "emittedAt must not be null");
    }

    public UUID getId() {
        return id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public BigDecimal getEnergyDelta() {
        return energyDelta;
    }

    public Instant getEmittedAt() {
        return emittedAt;
    }
}
