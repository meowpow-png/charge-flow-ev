package io.github.meowpowpng.chargeflowev.billing.internal;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.domain.PricingRule;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    private SessionQuery sessionQuery;
    private UUID sessionId;

    @BeforeEach
    void setupBillingServiceTest() {
        sessionQuery = mock(SessionQuery.class);
        sessionId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should return correct result when calculating from FinalizedSession")
    void should_ReturnCorrectResult_when_CalculatingFromSession() {
        BillingCalculator calculator = mock(BillingCalculator.class);
        BillingService service = new BillingService(sessionQuery, calculator);
        BigDecimal energyTotal = new BigDecimal("12.500");

        FinalizedSession session = mock(FinalizedSession.class);
        when(session.getId()).thenReturn(sessionId);
        when(session.getEnergyTotal()).thenReturn(energyTotal);
        when(session.getType()).thenReturn(SessionType.RIDE);

        BillingResult expectedResult = mock(BillingResult.class);

        when(calculator.calculate(
                eq(sessionId),
                eq(energyTotal),
                any(PricingRule.class)
        )).thenReturn(expectedResult);

        BillingResult result = service.calculateForSession(session);
        assertThat(result).isSameAs(expectedResult);

        verify(calculator).calculate(
                eq(sessionId),
                eq(energyTotal),
                any(PricingRule.class)
        );
    }

    @Test
    @DisplayName("Should throw exception when session ID does not resolve to session")
    void should_ThrowException_when_SessionNotFound() {
        BillingCalculator calculator = new BillingCalculator();
        BillingService service = new BillingService(sessionQuery, calculator);

        when(sessionQuery.findFinalizedById(sessionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.calculateForSession(sessionId))
                .isInstanceOf(IllegalStateException.class);

        verify(sessionQuery).findFinalizedById(sessionId);
    }

    @Test
    @DisplayName("Should use ride pricing rule when session type is RIDE")
    void should_UseRidePricingRule_when_SessionTypeIsRide() {
        BillingCalculator calculator = mock(BillingCalculator.class);
        BillingService service = new BillingService(sessionQuery, calculator);

        FinalizedSession session = mock(FinalizedSession.class);
        when(session.getId()).thenReturn(sessionId);
        when(session.getEnergyTotal()).thenReturn(BigDecimal.ZERO);
        when(session.getType()).thenReturn(SessionType.RIDE);

        service.calculateForSession(session);

        var ruleCaptor = ArgumentCaptor.forClass(PricingRule.class);
        verify(calculator).calculate(any(), any(), ruleCaptor.capture());

        var actualUnitPrice = ruleCaptor.getValue().unitPrice();
        var expectedUnitPrice = BillingService.RIDE_PRICE;
        assertThat(actualUnitPrice).isEqualByComparingTo(expectedUnitPrice);
    }
}
