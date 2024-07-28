package com.example.movie_service.service;

import com.example.movie_service.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> searchMovies();

    Movie searchMovieById();
}
