package com.example.movie_service;

//import com.example.movie_service.config.ErrorResponseConfig;
import com.example.movie_service.config.ResponseConfig;
//import com.example.movie_service.config.SuccessResponseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ResponseConfig.class)
public class MovieServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApplication.class, args);
    }

}
