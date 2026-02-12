package io.github.meowpowpng.chargeflowev.session.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    @Test
    @DisplayName("Should create active session with zero energy when constructed")
    void should_CreateActiveSessionWithZeroEnergy_when_Constructed() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();
        SessionState state = SessionState.ACTIVE;

        Session session = new Session(id, type, startedAt, state);

        assertThat(session.getEnergyTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @DisplayName("Should throw exception when constructor argument is null")
    void should_ThrowException_when_ConstructorArgumentIsNull() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();
        SessionState state = SessionState.ACTIVE;

        assertNullPointerExceptionThrown(() ->
                new Session(null, type, startedAt, state)
        );
        assertNullPointerExceptionThrown(() ->
                new Session(id, null, startedAt, state)
        );
        assertNullPointerExceptionThrown(() ->
                new Session(id, type, null, state)
        );
        assertNullPointerExceptionThrown(() ->
                new Session(id, type, startedAt, null)
        );
    }

    @Test
    @DisplayName("Should finalize and set end timestamp when finalizeSession is called")
    void should_FinalizeAndSetEndTimestamp_when_FinalizeSessionIsCalled() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();

        Session session = new Session(id, type, startedAt, SessionState.ACTIVE);

        Instant fixedInstant = startedAt.plusSeconds(60);
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        session.finalizeSession(fixedClock);

        assertThat(session.isFinalized()).isTrue();
        assertThat(session.getEndedAt()).isEqualTo(fixedInstant);
    }

    @Test
    @DisplayName("Should throw exception when finalizing already finalized session")
    void should_ThrowException_when_FinalizingAlreadyFinalizedSession() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();

        Session session = new Session(id, type, startedAt, SessionState.ACTIVE);

        Instant fixedInstant = startedAt.plusSeconds(60);
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        session.finalizeSession(fixedClock);

        assertIllegalStateExceptionThrown(() -> session.finalizeSession(fixedClock));
    }

    @Test
    @DisplayName("Should throw exception when mutating after finalization")
    void should_ThrowException_when_MutatingAfterFinalization() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();

        Session session = new Session(id, type, startedAt, SessionState.ACTIVE);

        Instant fixedInstant = startedAt.plusSeconds(60);
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        session.finalizeSession(fixedClock);

        assertIllegalStateExceptionThrown(() ->
                session.addEnergy(BigDecimal.ONE)
        );
        assertIllegalStateExceptionThrown(() ->
                session.setEndedAt(fixedInstant.plusSeconds(10))
        );
        assertIllegalStateExceptionThrown(() ->
                session.finalizeSession(fixedClock)
        );
    }

    @Test
    @DisplayName("Should update energy total when setEnergyTotal is called")
    void should_UpdateEnergyTotal_when_setEnergyTotalCalled() {
        UUID id = UUID.randomUUID();
        SessionType type = SessionType.RIDE;
        Instant startedAt = Instant.now();

        Session session = new Session(id, type, startedAt, SessionState.ACTIVE);

        BigDecimal newEnergyTotal = new BigDecimal("12.345");
        session.addEnergy(newEnergyTotal);

        assertThat(session.getEnergyTotal()).isEqualByComparingTo(newEnergyTotal);
    }

    private static void assertNullPointerExceptionThrown(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    private static void assertIllegalStateExceptionThrown(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(IllegalStateException.class);
    }
}
