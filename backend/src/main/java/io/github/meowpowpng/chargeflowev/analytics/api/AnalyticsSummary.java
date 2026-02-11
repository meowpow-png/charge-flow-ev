package io.github.meowpowpng.chargeflowev.analytics.api;

import java.math.BigDecimal;
import java.util.Objects;

public record AnalyticsSummary(
        BigDecimal totalRideEnergy,
        BigDecimal totalChargingEnergy,
        BigDecimal totalRevenue,
        BigDecimal totalCost,
        BigDecimal netProfit,
        int rideCount,
        int chargingCount
) {
    public AnalyticsSummary {
        Objects.requireNonNull(totalRideEnergy, "totalRideEnergy must not be null");
        Objects.requireNonNull(totalChargingEnergy, "totalChargingEnergy must not be null");
        Objects.requireNonNull(totalRevenue, "totalRevenue must not be null");
        Objects.requireNonNull(totalCost, "totalCost must not be null");
        Objects.requireNonNull(netProfit, "netProfit must not be null");
        if (rideCount < 0) {
            throw new IllegalArgumentException("rideCount must not be negative value");
        }
        if (chargingCount < 0) {
            throw new IllegalArgumentException("chargingCound must not be negative value");
        }
    }
}
