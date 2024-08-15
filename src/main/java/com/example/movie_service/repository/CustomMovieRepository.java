package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;

import java.util.List;

public interface CustomMovieRepository {
    List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction);
//    List<MovieSearchResultDTO> searchMoviesByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction);
}
