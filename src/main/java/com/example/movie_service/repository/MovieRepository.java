package com.example.movie_service.repository;

import com.example.movie_service.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String>, CustomMovieRepository {
}
