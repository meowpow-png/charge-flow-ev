package io.github.meowpowpng.chargeflowev.billing.internal;

import io.github.meowpowpng.chargeflowev.billing.api.BillingQuery;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.domain.PricingRule;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class BillingService implements BillingQuery {

    private final PricingRule ridePricingRule;
    private final PricingRule chargingPricingRule;

    private final SessionQuery sessionQuery;
    private final BillingCalculator calculator;

    BillingService(
            SessionQuery sessionQuery,
            BillingCalculator calculator,
            BillingProperties properties
    ) {
        this.sessionQuery = Objects.requireNonNull(sessionQuery, "sessionQuery must not be null");
        this.calculator = Objects.requireNonNull(calculator, "calculator must not be null");

        this.ridePricingRule = new PricingRule(properties.ridePrice());
        this.chargingPricingRule = new PricingRule(properties.chargingCost());
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
