package com.example.movie_service.integration;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.dataInitService.DataInitializerService;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;
import static com.example.movie_service.constants.TestConstant.*;
import static com.example.movie_service.constants.TestConstant.ORDER_BY_TITLE;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieServiceImplIT {

    @Autowired
    private MovieServiceImpl movieServiceImpl;

    @Autowired
    private DataInitializerService dataInitializerService;

    @BeforeEach
    public void beforeEach() {
        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.initializeData();
    }

    @AfterEach
    public void afterEach() {
        dataInitializerService.clearDatabase();
    }

    @Test
    void testMoviesFoundWithTitleOnly() {
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = movieServiceImpl.searchMovies
                (EXISTED_MOVIE_TITLE, null, null, null, 10, 0, ORDER_BY_TITLE, ASC);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MOVIE_FOUND_CODE, responseEntity.getBody().getCode());
        assertEquals(MOVIE_FOUND_MESSAGE, responseEntity.getBody().getMessage());
        List<MovieSearchResultDTO> moviesList = responseEntity.getBody().getData();
        // Verify the movies
        assertEquals(3, moviesList.size());
        assertEquals(THE_DARK_KNIGHT, moviesList.get(0).getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, moviesList.get(1).getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, moviesList.get(2).getTitle());
    }

    @Test
    void noMovieFound() {
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = movieServiceImpl.searchMovies
                (NON_EXISTED_MOVIE_TITLE, null, null, null, 10, 0, ORDER_BY_TITLE, ASC);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MOVIE_NOT_FOUND_CODE, responseEntity.getBody().getCode());
        assertEquals(MOVIE_NOT_FOUND_MESSAGE, responseEntity.getBody().getMessage());
        List<MovieSearchResultDTO> moviesList = responseEntity.getBody().getData();
        // Verify the movies
        assertTrue(moviesList.isEmpty());
    }

    @Test
    void missingTitle() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EMPTY_STRING, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(MISSING_TITLE_CODE, exception.getErrorCode());
        assertEquals(MISSING_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void nullTitle() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                null, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(MISSING_TITLE_CODE, exception.getErrorCode());
        assertEquals(MISSING_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidYear() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, INVALID_YEAR_2030, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(INVALID_YEAR_CODE, exception.getErrorCode());
        assertEquals(INVALID_YEAR_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidLimit() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 5, 0, ORDER_BY_TITLE, ASC));
            assertEquals(INVALID_LIMIT_CODE, exception.getErrorCode());
        assertEquals(INVALID_LIMIT_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidOrderBy() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, "director", ASC));
        assertEquals(INVALID_ORDER_BY_CODE, exception.getErrorCode());
        assertEquals(INVALID_ORDER_BY_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidPage() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, -1, ORDER_BY_TITLE, ASC));
        assertEquals(INVALID_PAGE_CODE, exception.getErrorCode());
        assertEquals(INVALID_PAGE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidDirection() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, "up"));
        assertEquals(INVALID_DIRECTION_CODE, exception.getErrorCode());
        assertEquals(INVALID_DIRECTION_MESSAGE, exception.getErrorMessage());
    }
}
