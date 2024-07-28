package com.example.movie_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix="responses")
public class ResponseConfig {

    private Map<String, ResponseMessage> success;
    private Map<String, ResponseMessage> error;

    @Data
    public static class ResponseMessage{
        private int status;
        private int code;
        private String message;
    }
}
