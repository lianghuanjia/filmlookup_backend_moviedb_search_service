package com.example.movie_service.builder;

import lombok.Builder;
import lombok.Getter;

/**
 * <p>
 * This class is to encapsulate the parameters that pass from the MovieController to MovieServiceImpl, and
 * from MovieServiceImpl to CustomMovieRepositoryImpl
 * </p>
 * <p>
 * Because the required parameters of MovieServiceImpl class's searchMovies and CustomMovieRepositoryImpl class's
 * searchMovies exceed 7, putting all of them in the method can be error-prompted, therefore we use this class to
 * encapsulate all the required parameters, and pass them into those methods
 * </p>
 * @see com.example.movie_service.service.MovieServiceImpl#searchMovies(MovieSearchParam); 
 * @see com.example.movie_service.repository.CustomMovieRepositoryImpl#searchMovies(MovieSearchParam);
 */
@Getter
@Builder(toBuilder = true) // Enable toBuilder for making modifications
public class MovieSearchParam {
    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;
}
