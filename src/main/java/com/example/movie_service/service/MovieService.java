package com.example.movie_service.service;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.response.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
    ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction);

}
