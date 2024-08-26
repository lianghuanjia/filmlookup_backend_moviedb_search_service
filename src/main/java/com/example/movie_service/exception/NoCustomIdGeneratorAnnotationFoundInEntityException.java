package com.example.movie_service.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * This Exception is thrown when an entity is meant to use @CustomIdGeneratorAnnotation, but the
 * CustomIdGeneratorAnnotation is not found in the entity.
 * <br>
 * @see com.example.movie_service.annotation.CustomIdGeneratorAnnotation;
 */
@Getter
@Setter
public class NoCustomIdGeneratorAnnotationFoundInEntityException extends RuntimeException {
    private static final int ERROR_CODE = 500;
    private final String errorMessage;

    public NoCustomIdGeneratorAnnotationFoundInEntityException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return ERROR_CODE;
    }
}
