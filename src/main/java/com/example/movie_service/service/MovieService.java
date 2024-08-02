package com.example.movie_service.service;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;

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
    List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction);

    /**
     * Searches for a movie by its ID.
     *
     * @param id the ID of the movie to search for
     * @return the movie that matches the given ID
     */
    Movie searchMovieById(String id);
}
