package io.github.meowpowpng.chargeflowev.demo.application;

import io.github.meowpowpng.chargeflowev.billing.api.BillingQuery;
import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.demo.api.DemoRequest;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;
import io.github.meowpowpng.chargeflowev.session.api.SessionCommand;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;

import io.github.meowpowpng.chargeflowev.telemetry.api.TelemetryCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class DemoService {

    private static final BigDecimal RIDE_DELTA = new BigDecimal("0.05");
    private static final BigDecimal CHARGING_DELTA = new BigDecimal("0.08");

    private final SessionQuery sessionQuery;
    private final SessionCommand sessionCommand;
    private final TelemetryCommand telemetryCommand;
    private final BillingQuery billingQuery;

    public DemoService(
            SessionQuery sessionQuery,
            SessionCommand sessionCommand,
            TelemetryCommand telemetryCommand,
            BillingQuery billingQuery
    ){
        Objects.requireNonNull(sessionQuery, "sessionQuery must not be null");
        Objects.requireNonNull(sessionCommand, "sessionCommand must not be null");
        Objects.requireNonNull(telemetryCommand, "telemetryCommand must not be null");
        Objects.requireNonNull(billingQuery, "billingQuery must not be null");

        this.sessionQuery = sessionQuery;
        this.sessionCommand = sessionCommand;
        this.telemetryCommand = telemetryCommand;
        this.billingQuery = billingQuery;
    }

    @Transactional
    public DemoResponse run(DemoRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        if (sessionQuery.hasActiveSession()) {
            throw new DemoConflictException("Cannot run demo while a session is active");
        }
        // start ride session
        UUID rideSessionId = sessionCommand.startSession(SessionType.RIDE);

        // emit ride telemetry
        for (int i = 0; i < request.rideSamples(); i++) {
            sessionCommand.addEnergy(rideSessionId, RIDE_DELTA);
            telemetryCommand.recordTelemetry(rideSessionId, RIDE_DELTA);
        }
        // finalize ride session
        FinalizedSession rideSession = sessionCommand.finalizeSession(rideSessionId);

        // start charging session
        UUID chargingSessionId = sessionCommand.startSession(SessionType.CHARGING);

        // emit charging telemetry
        for (int i = 0; i < request.chargingSamples(); i++) {
            sessionCommand.addEnergy(chargingSessionId, CHARGING_DELTA);
            telemetryCommand.recordTelemetry(chargingSessionId, CHARGING_DELTA);
        }
        // finalize charging session
        FinalizedSession chargingSession = sessionCommand.finalizeSession(chargingSessionId);

        BillingResult rideBilling = billingQuery.calculateForSession(rideSession);
        BillingResult chargingBilling = billingQuery.calculateForSession(chargingSession);

        return new DemoResponse(
                rideSessionId,
                chargingSessionId,
                rideBilling,
                chargingBilling
        );
    }
}
