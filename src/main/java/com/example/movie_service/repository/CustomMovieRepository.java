package com.example.movie_service.repository;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;

import java.util.List;

/**
 * Interface of custom movie repository layer
 */
public interface CustomMovieRepository {
    List<MovieSearchResultDTO> searchMovies(MovieSearchParam movieSearchParam);
    OneMovieDetailsDTO searchOneMovieDetails(String movieId);
}
