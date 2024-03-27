package com.oleksa.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyAppConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;
    private final String basePath;

    @Autowired
    public MyAppConfig(
            @Value("${allowed.origins}") String[] allowedOrigins,
            @Value("${spring.data.rest.base-path}") String basePath
    ) {
        this.allowedOrigins = allowedOrigins;
        this.basePath = "/api";
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // set up cors mapping
        registry.addMapping(basePath + "/**").allowedOrigins(allowedOrigins);
    }
}
