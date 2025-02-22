@Configuration
public class ModelConfig {
    @Value("${ml.model-path}")
    private String modelPath;
    
    @Bean
    public ModelRegistry modelRegistry() {
        // Handle model versioning
        // Support A/B testing
        // Enable easy rollback
    }
} 