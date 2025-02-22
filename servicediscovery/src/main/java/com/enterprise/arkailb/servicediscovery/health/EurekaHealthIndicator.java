package com.enterprise.arkailb.servicediscovery.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.stereotype.Component;

@Component
public class EurekaHealthIndicator implements HealthIndicator {

    private final EurekaServerConfigBean eurekaServerConfig;

    public EurekaHealthIndicator(EurekaServerConfigBean eurekaServerConfig) {
        this.eurekaServerConfig = eurekaServerConfig;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("enableSelfPreservation", eurekaServerConfig.shouldEnableSelfPreservation())
                .withDetail("renewalThreshold", eurekaServerConfig.getRenewalPercentThreshold())
                .withDetail("renewalIntervalInSecs", eurekaServerConfig.getEvictionIntervalTimerInMs() / 1000)
                .build();
    }
}