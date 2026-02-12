package io.github.meowpowpng.chargeflowev.billing.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PricingRule(BigDecimal unitPrice) {

    public PricingRule {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }
}
