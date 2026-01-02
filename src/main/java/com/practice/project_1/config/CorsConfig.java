package com.practice.project_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Apply to all endpoints
                        .allowedOriginPatterns("*")  // Pattern to allow all origins (fixes wildcard issue)
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")  // All needed methods
                        .allowedHeaders("*")  // All headers (e.g., Authorization, Content-Type)
                        .exposedHeaders("*")  // Expose headers if needed
                        .allowCredentials(true)  // Keep if you might use cookies; set to false if not
                        .maxAge(3600);  // Cache preflight for 1 hour
            }
        };
    }
}
