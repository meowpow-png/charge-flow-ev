package io.github.meowpowpng.chargeflowev.billing.api;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.billing.internal.BillingService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/billing")
public final class BillingController {

    private final BillingService service;

    BillingController(BillingService service) {
        Objects.requireNonNull(service, "service must not be null");
        this.service = service;
    }

    @GetMapping("/session/{sessionId}")
    public BillingResult getBillingForSession(@PathVariable UUID sessionId) {
        return service.calculateForSession(sessionId);
    }
}
