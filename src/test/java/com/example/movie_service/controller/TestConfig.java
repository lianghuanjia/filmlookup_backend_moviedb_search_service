package com.example.movie_service.controller;

import com.example.movie_service.config.ResponseConfig;
import com.example.movie_service.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;

public class TestConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ResponseConfig responseConfig){
        return new GlobalExceptionHandler(responseConfig);
    }

    @Bean
    public ResponseConfig responseConfig() {
        return new ResponseConfig();
    }
}
