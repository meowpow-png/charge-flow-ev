package io.github.meowpowpng.chargeflowev.session.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private SessionType type;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Nullable
    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "energy_total", precision = 10, scale = 3)
    private BigDecimal energyTotal;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private SessionState state;

    protected Session() {}

    public Session(UUID id, SessionType type, Instant startedAt, SessionState state) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.startedAt = Objects.requireNonNull(startedAt, "startedAt must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public SessionType getType() {
        return type;
    }

    @NonNull
    public Instant getStartedAt() {
        return startedAt;
    }

    @Nullable
    public Instant getEndedAt() {
        return endedAt;
    }

    @NonNull
    public BigDecimal getEnergyTotal() {
        return energyTotal;
    }

    @NonNull
    public SessionState getState() {
        return state;
    }

    public boolean isFinalized() {
        return state == SessionState.FINALIZED;
    }

    public void setEndedAt(@NonNull Instant endedAt) {
        this.endedAt = endedAt;
    }

    public void setEnergyTotal(BigDecimal energyTotal) {
        this.energyTotal = energyTotal;
    }

    public void setState(SessionState state) {
        this.state = state;
    }
}
