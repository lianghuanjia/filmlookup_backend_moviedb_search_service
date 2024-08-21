package com.example.movie_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Custom exception class for validation errors.
 * This exception is thrown when a validation error occurs.
 */
@Data
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private int errorCode;
    private String errorMessage;
}
