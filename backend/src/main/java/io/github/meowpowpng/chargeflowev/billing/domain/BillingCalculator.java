package io.github.meowpowpng.chargeflowev.billing.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @implNote
 * Cost derivation is deterministic and does not apply
 * rounding or normalization. Presentation or financial
 * rounding policy is outside the scope of this demo.
 */
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
