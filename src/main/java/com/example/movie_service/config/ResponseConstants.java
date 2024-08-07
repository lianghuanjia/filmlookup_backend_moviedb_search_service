package com.example.movie_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix="responses")
public class ResponseConstants {

    private Map<String, ResponseCodeAndMessage> success;
    private Map<String, ResponseCodeAndMessage> error;

    @Data
    public static class ResponseCodeAndMessage {
        private int code;
        private String message;
    }
}
