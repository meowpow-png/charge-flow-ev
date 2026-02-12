package io.github.meowpowpng.chargeflowev.analytics.internal;

import io.github.meowpowpng.chargeflowev.analytics.api.AnalyticsSummary;
import io.github.meowpowpng.chargeflowev.billing.api.BillingQuery;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class AnalyticsService {

    private final SessionQuery sessionQuery;
    private final BillingQuery billingQuery;

    AnalyticsService(SessionQuery sessionQuery, BillingQuery billingQuery) {
        Objects.requireNonNull(sessionQuery, "sessionQuery must not be null");
        Objects.requireNonNull(billingQuery, "billingQuery must not be null");

        this.sessionQuery = sessionQuery;
        this.billingQuery = billingQuery;
    }

    public AnalyticsSummary getSummary() {
        List<FinalizedSession> sessions = sessionQuery.findAllFinalized();

        BigDecimal totalRideEnergy = BigDecimal.ZERO;
        BigDecimal totalChargingEnergy = BigDecimal.ZERO;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        int rideCount = 0;
        int chargingCount = 0;

        for (FinalizedSession session : sessions) {
            var billingResult = billingQuery.calculateForSession(session);
            if (session.getType() == SessionType.RIDE) {
                totalRideEnergy = totalRideEnergy.add(session.getEnergyTotal());
                totalRevenue = totalRevenue.add(billingResult.getTotalCost());
                rideCount++;
            }
            if (session.getType() == SessionType.CHARGING) {
                totalChargingEnergy = totalChargingEnergy.add(session.getEnergyTotal());
                totalCost = totalCost.add(billingResult.getTotalCost());
                chargingCount++;
            }
        }
        BigDecimal netProfit = totalRevenue.subtract(totalCost);
        return new AnalyticsSummary(
                totalRideEnergy,
                totalChargingEnergy,
                totalRevenue,
                totalCost,
                netProfit,
                rideCount,
                chargingCount
        );
    }
}
