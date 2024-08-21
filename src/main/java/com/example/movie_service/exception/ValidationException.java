package com.example.movie_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception class for validation errors.
 * This exception is thrown when a validation error occurs.
 */
@Getter
@Setter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;
}
