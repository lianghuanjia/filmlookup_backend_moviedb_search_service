package com.example.movie_service.service;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultWithPaginationDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.response.CustomResponse;
import org.springframework.http.ResponseEntity;


/**
 * Service interface for managing movies.
 * Provides methods for movie_service endpoints.
 */
public interface MovieService {

    /**
     * Searches for movies based on various criteria.
     *
     * @return a list of movies that match the search criteria
     */
    ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> searchMovies(MovieSearchParam movieSearchParam);

    ResponseEntity<CustomResponse<OneMovieDetailsDTO>> searchOneMovieDetails(String movieId);
}
