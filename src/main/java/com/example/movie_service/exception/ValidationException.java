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

    /**
     * The type of validation_error_type must match the error type in the application.yaml.
     * Examples: "invalid_year", "invalid_title".
     *          When the ValidationExceptionHandler in GlobalExceptionHandler catches this, it will retrieve the
     *          validation_error_type, and look up the corresponding response code and message in the application.yaml
     *          file. For example, it the validation_error_type is invalid_year, ValidationExceptionHandler will get the
     *          following from the application.yaml:
     *              invalid_year:
     *                  status: 400
     *                  code: 40001
     *                  message: "Invalid year"
     */
    private int errorCode;
    private String errorMessage;
}
