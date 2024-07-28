package com.example.movie_service.service;

import com.example.movie_service.entity.Movie;
import com.example.movie_service.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImplementation implements MovieService {

    private MovieRepository movieRepository;

    public MovieServiceImplementation() {
    }

    @Autowired
    public MovieServiceImplementation(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> searchMovies() {
        return null;
    }

    @Override
    public Movie searchMovieById() {
        return null;
    }
}
