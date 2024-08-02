package com.example.movie_service.service;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.repository.CustomMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of MovieService interface
 */
@Service
public class MovieServiceImpl implements MovieService {

    private CustomMovieRepository movieRepository;

    // Default constructor
    public MovieServiceImpl() {
    }

    /**
     * Constructor with dependencies injection.
     * @param movieRepository the repository for accessing movie data
     */
    @Autowired
    public MovieServiceImpl(CustomMovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Searches for movies based on various criteria.
     * @return a list of movies that match the search criteria
     */
    @Override
    public List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction) {
        if (limit <= 0) {
            limit = 10;
        }
        if (page < 0) {
            page = 0;
        }
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "primaryTitle";
        }
        if (direction == null || direction.isEmpty()) {
            direction = "asc";
        }

        return movieRepository.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
    }

    /**
     * Searches for a movie by its ID.
     * @param id the ID of the movie to search for
     * @return the movie that matches the given ID
     */
    @Override
    public Movie searchMovieById(String id) {
        return null;
    }
}
