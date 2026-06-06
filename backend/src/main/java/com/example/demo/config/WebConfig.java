package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:5173}")
    private String allowedOrigins;

    @Value("${CORS_MAX_AGE:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                .exposedHeaders("X-Total-Count")
                .allowCredentials(true)
                .maxAge(maxAge);
    }
}
