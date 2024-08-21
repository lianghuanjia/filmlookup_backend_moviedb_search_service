package com.example.movie_service.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final int errorCode;
    private final String errorMessage;
}
