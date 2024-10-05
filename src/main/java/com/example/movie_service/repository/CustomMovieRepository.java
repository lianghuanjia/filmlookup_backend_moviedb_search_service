package com.example.movie_service.repository;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchWithTitleDTOFromRepoToService;
import com.example.movie_service.dto.OneMovieDetailsDTO;

/**
 * Interface of custom movie repository layer
 */
public interface CustomMovieRepository {
    MovieSearchWithTitleDTOFromRepoToService searchMovies(MovieSearchParam movieSearchParam);
    OneMovieDetailsDTO searchOneMovieDetails(String movieId);
}
