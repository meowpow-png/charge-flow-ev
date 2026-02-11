package io.github.meowpowpng.chargeflowev.telemetry.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelemetryTest {

    @Test
    @DisplayName("Should normalize energy delta when constructed")
    void should_NormalizeEnergyDelta_when_Constructed() {
        UUID id = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        BigDecimal input = new BigDecimal("1.2346");
        Instant emittedAt = Instant.now();

        Telemetry telemetry = new Telemetry(id, sessionId, input, emittedAt);

        var actualDelta = telemetry.getEnergyDelta();
        var expectedDelta = new BigDecimal("1.235");
        assertThat(actualDelta).isEqualByComparingTo(expectedDelta);
    }

    @Test
    @DisplayName("Should throw exception when energy delta is negative")
    void should_ThrowException_when_EnergyDeltaIsNegative() {
        UUID id = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        BigDecimal negativeDelta = new BigDecimal("-1.000");
        Instant emittedAt = Instant.now();

        assertIllegalArgumentException(() ->
                new Telemetry(id, sessionId, negativeDelta, emittedAt)
        );
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @DisplayName("Should throw exception when required argument is null")
    void should_ThrowException_when_RequiredArgumentIsNull() {
        UUID id = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        BigDecimal energyDelta = new BigDecimal("1.000");
        Instant emittedAt = Instant.now();

        assertNullPointerExceptionThrown(() ->
                new Telemetry(null, sessionId, energyDelta, emittedAt)
        );
        assertNullPointerExceptionThrown(() ->
                new Telemetry(id, null, energyDelta, emittedAt)
        );
        assertNullPointerExceptionThrown(() ->
                new Telemetry(id, sessionId, null, emittedAt)
        );
        assertNullPointerExceptionThrown(() ->
                new Telemetry(id, sessionId, energyDelta, null)
        );
    }

    private static void assertNullPointerExceptionThrown(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    private static void assertIllegalArgumentException(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
