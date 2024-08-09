package com.example.movie_service.service;

import com.example.movie_service.config.ResponseConstants;
import com.example.movie_service.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;

public class TestConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ResponseConstants responseConstants){
        return new GlobalExceptionHandler(responseConstants);
    }

    @Bean
    public ResponseConstants responseConfig() {
        return new ResponseConstants();
    }
}
