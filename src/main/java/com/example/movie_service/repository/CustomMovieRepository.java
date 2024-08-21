package com.example.movie_service.repository;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;

import java.util.List;

public interface CustomMovieRepository {
    List<MovieSearchResultDTO> searchMovies(MovieSearchParam movieSearchParam);
}
