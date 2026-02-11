package io.github.meowpowpng.chargeflowev.billing.internal;

import io.github.meowpowpng.chargeflowev.billing.api.BillingQuery;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.domain.PricingRule;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class BillingService implements BillingQuery {

    // TODO: Externalize into application configuration instead of hardcoding
    private static final BigDecimal RIDE_PRICE = new BigDecimal("0.30");
    private static final BigDecimal CHARGING_COST = new BigDecimal("0.10");

    private final PricingRule ridePricingRule = new PricingRule(RIDE_PRICE);
    private final PricingRule chargingPricingRule = new PricingRule(CHARGING_COST);

    private final SessionQuery sessionQuery;
    private final BillingCalculator calculator;

    public BillingService(SessionQuery sessionQuery, BillingCalculator calculator) {
        this.sessionQuery = Objects.requireNonNull(sessionQuery, "sessionQuery must not be null");
        this.calculator = Objects.requireNonNull(calculator, "calculator must not be null");
    }

    @Override
    public BillingResult calculateForSession(FinalizedSession session) {
        PricingRule rule = switch (session.getType()) {
            case RIDE -> ridePricingRule;
            case CHARGING -> chargingPricingRule;
        };
        return calculator.calculate(session.getId(), session.getEnergyTotal(), rule);
    }

    public BillingResult calculateForSession(UUID sessionId) {
        var session = sessionQuery.findFinalizedById(sessionId).orElseThrow(() ->
            new IllegalStateException("Finalized session not found (id=" + sessionId + ')')
        );
        return calculateForSession(session);
    }
}
