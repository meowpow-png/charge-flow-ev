package io.github.meowpowpng.chargeflowev.billing;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.domain.PricingRule;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BillingCalculatorTest {

    @Test
    @DisplayName("Should calculate total cost from billable energy and unit price")
    void should_CalculateTotalCost_when_ValidInputsAreProvided() {
        BillingCalculator calculator = new BillingCalculator();
        UUID sessionId = UUID.randomUUID();

        BigDecimal billableEnergy = new BigDecimal("10.0");
        BigDecimal unitPrice = new BigDecimal("0.25");
        PricingRule pricingRule = new PricingRule(unitPrice);

        BillingResult result = calculator.calculate(sessionId, billableEnergy, pricingRule);

        assertThat(result.getSessionId()).isEqualTo(sessionId);
        assertThat(result.getBillableEnergy()).isEqualTo(billableEnergy);
        assertThat(result.getUnitPrice()).isEqualTo(unitPrice);

        var expectedTotalCost = billableEnergy.multiply(unitPrice);
        assertThat(result.getTotalCost()).isEqualByComparingTo(expectedTotalCost);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @DisplayName("Should throw exception when any required input is null")
    void should_ThrowException_when_InputIsNull() {
        BillingCalculator calculator = new BillingCalculator();
        UUID sessionId = UUID.randomUUID();
        BigDecimal billableEnergy = new BigDecimal("10.0");
        PricingRule pricingRule = new PricingRule(new BigDecimal("0.25"));

        assertNullPointerExceptionThrown(() ->
                calculator.calculate(null, billableEnergy, pricingRule)
        );
        assertNullPointerExceptionThrown(() ->
                calculator.calculate(sessionId, null, pricingRule)
        );
        assertNullPointerExceptionThrown(() ->
                calculator.calculate(sessionId, billableEnergy, null)
        );
    }

    private static void assertNullPointerExceptionThrown(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }
}
