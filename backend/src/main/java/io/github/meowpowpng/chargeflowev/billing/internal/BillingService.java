package io.github.meowpowpng.chargeflowev.billing.internal;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.domain.PricingRule;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.Session;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class BillingService {

    // TODO: Externalize into application configuration instead of hardcoding
    private static final BigDecimal UNIT_PRICE = new BigDecimal("0.25");

    private final SessionQuery sessionQuery;
    private final BillingCalculator calculator;
    private final PricingRule pricingRule;

    public BillingService(SessionQuery sessionQuery, BillingCalculator calculator) {
        this.sessionQuery = Objects.requireNonNull(sessionQuery, "sessionQuery must not be null");
        this.calculator = Objects.requireNonNull(calculator, "calculator must not be null");
        this.pricingRule = new PricingRule(UNIT_PRICE);
    }

    public BillingResult calculateForSession(UUID sessionId) {
        Session session = sessionQuery.findFinalizedById(sessionId).orElseThrow(() -> {
            var message = "Finalized session not found (id=" + sessionId.toString() + ')';
            return new IllegalStateException(message);
        });
        return calculator.calculate(session.getId(), session.getEnergyTotal(), pricingRule);
    }
}
