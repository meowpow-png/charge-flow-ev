package io.github.meowpowpng.chargeflowev.billing.internal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "chargeflow.billing")
public record BillingProperties(
        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal ridePrice,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal chargingCost
) {}
