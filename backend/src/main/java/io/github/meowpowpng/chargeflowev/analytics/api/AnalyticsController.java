package io.github.meowpowpng.chargeflowev.analytics.api;

import io.github.meowpowpng.chargeflowev.analytics.internal.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/analytics")
public final class AnalyticsController {

    private final AnalyticsService service;

    AnalyticsController(AnalyticsService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @GetMapping("/summary")
    public AnalyticsSummary getSummary() {
        return service.getSummary();
    }
}
