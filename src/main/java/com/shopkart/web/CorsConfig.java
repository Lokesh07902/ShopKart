package com.shopkart.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SESSION 30 — Without this, the browser blocks requests from the frontend
 * (served on a different origin, e.g. http://127.0.0.1:5500 via VS Code's
 * Live Server) to the API (http://localhost:8080) under the Same-Origin
 * Policy. This opens /api/** to any origin — fine for a local student
 * project; a real deployment would restrict `allowedOrigins` to the actual
 * frontend domain instead of "*".
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
