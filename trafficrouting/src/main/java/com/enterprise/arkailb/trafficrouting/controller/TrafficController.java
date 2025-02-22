package com.enterprise.arkailb.trafficrouting.controller;

import com.enterprise.arkailb.trafficrouting.service.AiModelService;
import com.enterprise.arkailb.trafficrouting.service.CloudRoutingService;
import com.enterprise.arkailb.trafficrouting.service.MetricsService;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;

@RestController
public class TrafficController {

    @Autowired
    private AiModelService aiModelService;
    
    @Autowired
    private CloudRoutingService cloudRoutingService;
    
    @Autowired
    private MetricsService metricsService;

    private final Tracer tracer;

    @GetMapping("/route")
    public String routeTraffic(@RequestParam String trafficType) {
        Span rootSpan = tracer.spanBuilder("route-traffic")
            .setAttribute("traffic.type", trafficType)
            .startSpan();
        
        try (Scope scope = rootSpan.makeCurrent()) {
            String optimalInstance = aiModelService.predictOptimalInstance(trafficType);
            String cloudProvider = cloudRoutingService.determineOptimalCloud(trafficType);
            
            // Record metrics
            Timer.Sample sample = Timer.start();
            sample.stop(metricsService.getRoutingTimer());
            metricsService.recordRoutingDecision(cloudProvider, System.currentTimeMillis());
            
            return String.format("Routing to: %s in %s", optimalInstance, cloudProvider);
        } finally {
            rootSpan.end();
        }
    }
} 