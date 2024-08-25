package com.example.movie_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * It is the custom response for all endpoints.
 * @param <T> A generic type that represents the data.
 */
@Data
@AllArgsConstructor
public class CustomResponse<T> {
    // response code (e.g., 20001)
    private int code;

    // Response message (e.g., "Movie(s) found")
    private String message; // Response message (e.g., "Movie(s) found")

    // Data retrieved from service layer, it can be:
    //     - A list of MovieSearchResultDTO when movies found
    //     - An empty list when no movies found
    //     - Null when there are Exceptions thrown in the endpoints
    private T data;
}
