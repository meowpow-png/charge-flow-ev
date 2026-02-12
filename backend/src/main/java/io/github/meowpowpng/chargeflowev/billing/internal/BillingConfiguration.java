package io.github.meowpowpng.chargeflowev.billing.internal;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingCalculator;
import io.github.meowpowpng.chargeflowev.session.api.SessionQuery;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(BillingProperties.class)
public class BillingConfiguration {

    private final BillingProperties properties;

    public BillingConfiguration(BillingProperties properties) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
    }

    @Bean
    public BillingService billingService(SessionQuery sessionQuery, BillingCalculator calculator) {
        return new BillingService(sessionQuery, calculator, properties);
    }
}
