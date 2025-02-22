package com.enterprise.arkailb.trafficrouting.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    
    private final MeterRegistry registry;
    private final Timer routingTimer;

    public MetricsService(MeterRegistry registry) {
        this.registry = registry;
        this.routingTimer = Timer.builder("routing.decision.time")
                               .description("Time taken to make routing decisions")
                               .register(registry);
    }

    public void recordRoutingDecision(String cloudProvider, long latency) {
        registry.counter("routing.decisions", "cloud", cloudProvider).increment();
        registry.gauge("routing.latency", latency);
    }

    public Timer getRoutingTimer() {
        return routingTimer;
    }
} 