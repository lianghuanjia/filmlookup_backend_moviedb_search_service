package com.example.movie_service.service;


import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.response.CustomResponse;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

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
    void searchWithNullTitle() {
        title = null;
        //since underneath, it uses validationService, and this service is not what we are testing right now, we need to mock its behavior
        doThrow(ValidationException.class).when(validationService).validateTitle(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction));
    }

    @Test
    void searchWithInvalidYear() {
        releasedYear = "2039";

        doNothing().when(validationService).validateTitle(any());
        doThrow(ValidationException.class).when(validationService).validateReleasedYear(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidLimit(){
        Integer invalidLimit = 15;

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doThrow(ValidationException.class).when(validationService).validateLimit(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, invalidLimit, page, orderBy, direction),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidPage(){
        page = -1;

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doThrow(ValidationException.class).when(validationService).validatePage(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidOrderBy(){
        orderBy = "time";

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doThrow(ValidationException.class).when(validationService).validateOrderBy(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidDirection(){
        direction = "north";

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doThrow(ValidationException.class).when(validationService).validateDirection(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a ValidationException");
    }


    @Test
    void searchMovieTimeout() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(QueryTimeoutException.class).when(movieRepository).searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        assertThrows(QueryTimeoutException.class, ()-> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a QueryTimeoutException");
    }

    @Test
    void searchMovieThrowPersistenceException() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(PersistenceException.class).when(movieRepository).searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

        assertThrows(PersistenceException.class, ()-> movieServiceImpl.searchMovies
                (title, releasedYear, director, genre, limit, page, orderBy, direction),
                "searchMovies did not throw a PersistenceException");
    }

    @Test
    void searchMovieReturnListOfMovieSearchResultDTO() {
        // Setup mock data
        List<MovieSearchResultDTO> mockMovies = List.of(
                new MovieSearchResultDTO("1", "Inception", "2010", "Christopher Nolan", "path/to/backdrop", "path/to/poster", 9.0)
        );

        // Mock the repository call
        when(movieRepository.searchMovies(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockMovies);

        // Ensure validation service calls do nothing (if necessary)
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());

        // Call the actual method under test
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> actualResponseEntity = movieServiceImpl.searchMovies(
                title, releasedYear, director, genre, limit, page, orderBy, direction
        );

        // Verify the results
        assertNotNull(actualResponseEntity.getBody());
        assertNotNull(actualResponseEntity.getBody().getData());
        assertEquals(1, actualResponseEntity.getBody().getData().size());
        assertEquals("Inception", actualResponseEntity.getBody().getData().get(0).getTitle());
    }


    @Test
    void searchMovieWithNoResult() {
        List<MovieSearchResultDTO> mockMovies = List.of();

        // Mock the repository call
        when(movieRepository.searchMovies(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockMovies);

        // Ensure validation service calls do nothing (if necessary)
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());


        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> actualResponseEntity = movieServiceImpl.searchMovies(
                title, releasedYear, director, genre, limit, page, orderBy, direction
        );

        assertNotNull(actualResponseEntity.getBody());
        assertTrue(actualResponseEntity.getBody().getData().isEmpty());
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(MOVIE_NOT_FOUND_CODE, actualResponseEntity.getBody().getCode());
        assertEquals(MOVIE_NOT_FOUND_MESSAGE, actualResponseEntity.getBody().getMessage());

    }



}
