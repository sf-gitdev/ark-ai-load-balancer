package com.enterprise.arkailb.trafficrouting.service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.opentelemetry.api.trace.StatusCode;

@Service
public class CloudRoutingService {
    
    private final Tracer tracer;
    private final MetricsService metricsService;

    @Autowired
    public CloudRoutingService(Tracer tracer, MetricsService metricsService) {
        this.tracer = tracer;
        this.metricsService = metricsService;
    }

    public String determineOptimalCloud(String trafficType) {
        Span span = tracer.spanBuilder("determine-optimal-cloud")
            .setAttribute("traffic.type", trafficType)
            .startSpan();
        
        try {
            // Add events to track important operations
            span.addEvent("starting-cloud-selection");
            
            String selectedCloud = switch(trafficType) {
                case "low-latency" -> {
                    span.setAttribute("selected.cloud", "azure");
                    yield "azure-eastus";
                }
                case "high-compute":
                    yield "aws-us-east-1"; // AWS for compute
                case "storage":
                    yield "gcp-us-central1"; // GCP for storage
                default -> {
                    span.setAttribute("selected.cloud", "aws");
                    yield "aws-us-east-1";
                }
            };
            
            span.addEvent("cloud-selection-complete");
            return selectedCloud;
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "Cloud selection failed");
            throw e;
        } finally {
            span.end();
        }
    }
} 