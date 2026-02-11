package io.github.meowpowpng.chargeflowev.telemetry.internal;

import io.github.meowpowpng.chargeflowev.telemetry.domain.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TelemetryRepository extends JpaRepository<Telemetry, UUID> {}
