package com.example.movie_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // @NonNull: If I don't use it, I will get a warning "Not annotated parameter overrides @NonNullApi parameter".
                // This warning occurs because Spring framework uses @NonNullApi at the package level, which means that
                // all method parameters and return values in that package are assumed to be non-null by default.
                // However, the CorsRegistry parameter in the addCorsMappings method is not explicitly annotated as
                // @NonNull, leading to a potential mismatch between the annotations and the actual method signature.
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5174")  // Allow your frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
