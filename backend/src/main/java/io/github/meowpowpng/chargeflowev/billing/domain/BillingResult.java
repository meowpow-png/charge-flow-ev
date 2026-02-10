package io.github.meowpowpng.chargeflowev.billing.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public final class BillingResult {

    private final UUID sessionId;
    private final BigDecimal billableEnergy;
    private final BigDecimal unitPrice;
    private final BigDecimal totalCost;

    BillingResult(
            UUID sessionId,
            BigDecimal billableEnergy,
            BigDecimal unitPrice,
            BigDecimal totalCost
    ) {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        this.sessionId = sessionId;
        Objects.requireNonNull(billableEnergy, "billableEnergy must not be null");
        this.billableEnergy = billableEnergy;

        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        this.totalCost = Objects.requireNonNull(totalCost, "totalCost must not be null");
    }

    public UUID sessionId() {
        return sessionId;
    }

    public BigDecimal billableEnergy() {
        return billableEnergy;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }

    public BigDecimal totalCost() {
        return totalCost;
    }
}
