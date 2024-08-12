package com.example.movie_service.exception;

import lombok.*;

@Data
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    private int errorCode;
    private String errorMessage;
}
