package com.example.movie_service.dto;

import com.example.movie_service.builder.MovieSearchParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class is to contain the returning searching movie's values retrieved from database.
 * @see com.example.movie_service.repository.CustomMovieRepositoryImpl#searchMovies(MovieSearchParam) 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchQueryDTO {
    private String id;
    private String title;
    private String releaseTime;
    private String directors;
    private String backdropPath;
    private String posterPath;
    private Double rating;
}
