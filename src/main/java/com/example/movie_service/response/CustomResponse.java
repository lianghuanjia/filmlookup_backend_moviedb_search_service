package com.example.movie_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * It is the standard response for all endpoints.
 * @param <T> A generic type of data retrieved from service layer
 */
@Data
@AllArgsConstructor
public class CustomResponse<T> {
    private int code;       // response code (e.g., 20001)
    private String message; // Response message (e.g., "Movie(s) found")
    private T data;         // Data retrieved from service layer (e.g., a list of Movie entities)
}
