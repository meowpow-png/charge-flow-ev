package io.github.meowpowpng.chargeflowev.demo.application;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;

import java.util.UUID;

public record DemoResponse(
        UUID rideSessionId,
        UUID chargingSessionId,
        BillingResult rideBilling,
        BillingResult chargingBilling
) {}
