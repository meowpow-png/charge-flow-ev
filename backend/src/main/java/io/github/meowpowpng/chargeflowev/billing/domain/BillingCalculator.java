package io.github.meowpowpng.chargeflowev.billing.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public final class BillingCalculator {

    public BillingResult calculate(
            UUID sessionId,
            BigDecimal billableEnergy,
            PricingRule pricingRule
    ) {
        BigDecimal unitPrice = pricingRule.unitPrice();
        BigDecimal totalCost = billableEnergy.multiply(unitPrice);

        return new BillingResult(sessionId, billableEnergy, unitPrice, totalCost);
    }
}
