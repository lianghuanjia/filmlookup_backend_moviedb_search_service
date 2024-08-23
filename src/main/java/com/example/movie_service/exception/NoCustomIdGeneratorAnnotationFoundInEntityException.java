package com.example.movie_service.exception;

import lombok.Getter;
import lombok.Setter;

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
