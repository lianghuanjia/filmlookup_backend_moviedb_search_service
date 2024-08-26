package com.example.movie_service.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This Exception is thrown when a resource is not found
 */
@Getter
@Setter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final int errorCode;
    private final String errorMessage;
}
