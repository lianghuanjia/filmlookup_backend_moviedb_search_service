package com.example.movie_service.service;

import com.example.movie_service.converter.MovieSearchResultConverter;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.repository.PersonRepository;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Spy
    private CustomMovieRepository movieRepository; // Should I make this final?

    @Spy
    private ValidationService validationService;

    @InjectMocks
    private MovieServiceImpl movieServiceImpl; // Should this be MovieService or MovieServiceImpl?

    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;


    @BeforeEach
    public void setUp() {
        title = "title";
        releasedYear = "2020";
        director = "director";
        genre = "genre";
        limit = 10;
        page = 0;
        orderBy = "orderBy";
        direction = "direction";
    }

    @Test
    public void searchWithNullTitle() {
        title = null;

        // Output I expect
        /**
         * movieServiceImpl -> searchMovies (what I want to test)
         *      -> validateSearchMoviesParameters
         *          since underneath it uses validationService, and this service is not what we are testing right now, we need to mock its behavior
         */
//        when(validationService.validateTitle(any())).thenThrow(ValidationException.class);

        doThrow(ValidationException.class).when(validationService).validateTitle(any());

        assertThrows(ValidationException.class, () -> {
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        });
    }

    @Test
    public void searchWithInvalidYear() {
        releasedYear = "2039";

        doNothing().when(validationService).validateTitle(any());
        doThrow(ValidationException.class).when(validationService).validateReleasedYear(any());

        assertThrows(ValidationException.class, () -> {
           movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        }, "searchMovies did not throw a ValidationException");
    }

    @Test
    public void searchWithInvalidLimit(){
        limit = 15;

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doThrow(ValidationException.class).when(validationService).validateLimit(any());

        assertThrows(ValidationException.class, () -> {
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        }, "searchMovies did not throw a ValidationException");
    }

    @Test
    public void searchWithInvalidPage(){
        page = -1;

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doThrow(ValidationException.class).when(validationService).validatePage(any());

        assertThrows(ValidationException.class, () -> {
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        }, "searchMovies did not throw a ValidationException");
    }

    @Test
    public void searchWithInvalidOrderBy(){
        orderBy = "time";

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doThrow(ValidationException.class).when(validationService).validateOrderBy(any());

        assertThrows(ValidationException.class, () -> {
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        }, "searchMovies did not throw a ValidationException");
    }

    @Test
    public void searchWithInvalidDirection(){
        direction = "north";

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doThrow(ValidationException.class).when(validationService).validateDirection(any());

        assertThrows(ValidationException.class, () -> {
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);
        }, "searchMovies did not throw a ValidationException");
    }


    @Test
    public void searchMovieTimeout() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(QueryTimeoutException.class).when(movieRepository).searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        assertThrows(QueryTimeoutException.class, ()->{
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        }, "searchMovies did not throw a QueryTimeoutException");
    }

    @Test
    public void searchMovieThrowPersistenceException() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(PersistenceException.class).when(movieRepository).searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        assertThrows(PersistenceException.class, ()->{
            movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        }, "searchMovies did not throw a PersistenceException");
    }



}
