package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionState;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SessionRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private SessionRepository repository;

    @Test
    @DisplayName("Should persist session when saved")
    void should_PersistSession_when_Saved() {
        UUID id = UUID.randomUUID();
        Instant startedAt = Instant.now();
        Session session = new Session(
                id,
                SessionType.RIDE,
                startedAt,
                SessionState.ACTIVE
        );
        repository.save(session);

        Optional<Session> result = repository.findById(id);
        assertThat(result).isPresent();

        Session persisted = result.orElseThrow();
        assertThat(persisted.getId()).isEqualTo(id);
        assertThat(persisted.getType()).isEqualTo(SessionType.RIDE);
        assertThat(persisted.getState()).isEqualTo(SessionState.ACTIVE);
        assertThat(persisted.getEnergyTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(persisted.getEndedAt()).isNull();
    }

    @Test
    @DisplayName("Should update session state and end timestamp when finalized")
    void should_UpdateSessionStateAndEndTimestamp_when_Finalized() {
        UUID id = UUID.randomUUID();
        Instant startedAt = Instant.now();
        Session session = new Session(
                id,
                SessionType.RIDE,
                startedAt,
                SessionState.ACTIVE
        );
        repository.save(session);

        session.finalizeSession(Clock.systemUTC());
        repository.save(session);

        Optional<Session> result = repository.findById(id);
        assertThat(result).isPresent();

        Session updated = result.orElseThrow();
        assertThat(updated.getState()).isEqualTo(SessionState.FINALIZED);
        assertThat(updated.getEndedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should return true when session with given state exists")
    void should_ReturnTrue_when_SessionWithStateExists() {
        Session session = new Session(
                UUID.randomUUID(),
                SessionType.RIDE,
                Instant.now(),
                SessionState.ACTIVE
        );
        repository.save(session);

        boolean exists = repository.existsByState(SessionState.ACTIVE);
        assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("Should persist energy with scale three when saved")
    void should_PersistEnergyWithScaleThree_when_Saved() {
        UUID id = UUID.randomUUID();
        Session session = new Session(
                id,
                SessionType.RIDE,
                Instant.now(),
                SessionState.ACTIVE
        );
        session.addEnergy(new BigDecimal("1.23456"));

        repository.save(session);
        Optional<Session> result = repository.findById(id);

        assertThat(result).isPresent();
        Session persisted = result.orElseThrow();

        var actualEnergy = persisted.getEnergyTotal();
        var expectedEnergy = new BigDecimal("1.235");
        assertThat(actualEnergy).isEqualByComparingTo(expectedEnergy);
    }
}

