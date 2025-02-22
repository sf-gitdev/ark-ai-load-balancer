package com.enterprise.arkailb.mlservice.service;

import org.springframework.stereotype.Service;

@Service
public class ModelService {
    private final ModelLoader modelLoader;
    private final FeatureExtractor featureExtractor;
    
    public PredictionResult predict(TrafficData trafficData) {
        // Extract features
        // Load model
        // Make prediction
        // Return result with confidence score
    }
} 