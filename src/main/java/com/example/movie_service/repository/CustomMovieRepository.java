package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;

import java.util.List;

public interface CustomMovieRepository {
    List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, int limit, int page, String orderBy, String direction);
}
