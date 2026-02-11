package io.github.meowpowpng.chargeflowev.telemetry.internal;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionState;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import io.github.meowpowpng.chargeflowev.session.internal.SessionRepository;
import io.github.meowpowpng.chargeflowev.telemetry.domain.Telemetry;

import jakarta.persistence.EntityManager;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

@NullUnmarked
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelemetryRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private TelemetryRepository repository;

    @Autowired
    @SuppressWarnings("unused")
    private SessionRepository sessionRepository;

    @Autowired
    @SuppressWarnings("unused")
    private EntityManager entityManager;

    @Test
    @DisplayName("Should persist telemetry when saved")
    void should_PersistTelemetry_when_Saved() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session(
                sessionId,
                SessionType.RIDE,
                Instant.now(),
                SessionState.ACTIVE
        );
        sessionRepository.save(session);

        UUID telemetryId = UUID.randomUUID();
        Instant emittedAt = Instant.now();
        BigDecimal delta = new BigDecimal("1.250");

        Telemetry telemetry = new Telemetry(
                telemetryId,
                sessionId,
                delta,
                emittedAt
        );
        repository.save(telemetry);
        Optional<Telemetry> result = repository.findById(telemetryId);

        assertThat(result).isPresent();

        Telemetry persisted = result.orElseThrow();
        assertThat(persisted.getId()).isEqualTo(telemetryId);
        assertThat(persisted.getSessionId()).isEqualTo(sessionId);
        assertThat(persisted.getEnergyDelta()).isEqualByComparingTo(delta);
    }

    @Test
    @DisplayName("Should persist energy delta with scale three when saved")
    void should_PersistEnergyDeltaWithScaleThree_when_Saved() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session(
                sessionId,
                SessionType.RIDE,
                Instant.now(),
                SessionState.ACTIVE
        );
        sessionRepository.save(session);

        UUID telemetryId = UUID.randomUUID();
        BigDecimal rawDelta = new BigDecimal("1.23456");

        Telemetry telemetry = new Telemetry(
                telemetryId,
                sessionId,
                rawDelta,
                Instant.now()
        );
        repository.save(telemetry);
        Optional<Telemetry> result = repository.findById(telemetryId);

        assertThat(result).isPresent();
        Telemetry persisted = result.orElseThrow();

        var actualEnergyDelta = persisted.getEnergyDelta();
        var expectedEnergyDelta = new BigDecimal("1.235");
        assertThat(actualEnergyDelta).isEqualByComparingTo(expectedEnergyDelta);
    }

    @Test
    @DisplayName("Should reject telemetry when session does not exist")
    void should_RejectTelemetry_when_SessionDoesNotExist() {
        UUID nonExistentSessionId = UUID.randomUUID();
        Telemetry telemetry = new Telemetry(
                UUID.randomUUID(),
                nonExistentSessionId,
                new BigDecimal("1.000"),
                Instant.now()
        );
        Throwable thrown = catchThrowable(() ->
                repository.saveAndFlush(telemetry)
        );
        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should delete telemetry when session is deleted")
    void should_DeleteTelemetry_when_SessionIsDeleted() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session(
                sessionId,
                SessionType.RIDE,
                Instant.now(),
                SessionState.ACTIVE
        );
        sessionRepository.saveAndFlush(session);

        UUID telemetryId = UUID.randomUUID();
        Telemetry telemetry = new Telemetry(
                telemetryId,
                sessionId,
                new BigDecimal("1.000"),
                Instant.now()
        );
        repository.saveAndFlush(telemetry);

        sessionRepository.deleteById(sessionId);
        sessionRepository.flush();
        entityManager.clear();

        Optional<Telemetry> result = repository.findById(telemetryId);
        assertThat(result).isEmpty();
    }
}
