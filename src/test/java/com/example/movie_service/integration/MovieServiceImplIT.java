package com.example.movie_service.integration;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;
import static com.example.movie_service.constants.TestConstant.*;
import static com.example.movie_service.constants.TestConstant.ORDER_BY_TITLE;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieServiceImplIT {

    @SuppressWarnings({"resource"})
    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(SQL_VERSION)
            .withDatabaseName("testDB")
            .withUsername("testUser")
            .withPassword("testPassword")
            .withReuse(true);

    @DynamicPropertySource
    static void setUpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

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
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getCode(), MOVIE_FOUND_CODE);
        assertEquals(responseEntity.getBody().getMessage(), MOVIE_FOUND_MESSAGE);
        List<MovieSearchResultDTO> moviesList = responseEntity.getBody().getData();
        // Verify the movies
        assertEquals(moviesList.size(), 3);
        assertEquals(moviesList.get(0).getTitle(), THE_DARK_KNIGHT);
        assertEquals(moviesList.get(1).getTitle(), THE_DARK_KNIGHT_RISES);
        assertEquals(moviesList.get(2).getTitle(), THE_DARK_KNIGHT_RISES_AGAIN);
    }

    @Test
    void noMovieFound() {
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = movieServiceImpl.searchMovies
                (NON_EXISTED_MOVIE_TITLE, null, null, null, 10, 0, ORDER_BY_TITLE, ASC);

        // Verify the response
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getCode(), MOVIE_NOT_FOUND_CODE);
        assertEquals(responseEntity.getBody().getMessage(), MOVIE_NOT_FOUND_MESSAGE);
        List<MovieSearchResultDTO> moviesList = responseEntity.getBody().getData();
        // Verify the movies
        assertEquals(moviesList.size(), 0);
    }

    @Test
    void missingTitle() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EMPTY_STRING, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(exception.getErrorCode(), MISSING_TITLE_CODE);
        assertEquals(exception.getErrorMessage(), MISSING_TITLE_MESSAGE);
    }

    @Test
    void nullTitle() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                null, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(exception.getErrorCode(), MISSING_TITLE_CODE);
        assertEquals(exception.getErrorMessage(), MISSING_TITLE_MESSAGE);
    }

    @Test
    void invalidYear() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, INVALID_YEAR_2030, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, ASC));
        assertEquals(exception.getErrorCode(), INVALID_YEAR_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_YEAR_MESSAGE);
    }

    @Test
    void invalidLimit() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 5, 0, ORDER_BY_TITLE, ASC));
            assertEquals(exception.getErrorCode(), INVALID_LIMIT_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_LIMIT_MESSAGE);
    }

    @Test
    void invalidOrderBy() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, "director", ASC));
        assertEquals(exception.getErrorCode(), INVALID_ORDER_BY_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_ORDER_BY_MESSAGE);
    }

    @Test
    void invalidPage() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, -1, ORDER_BY_TITLE, ASC));
        assertEquals(exception.getErrorCode(), INVALID_PAGE_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_PAGE_MESSAGE);
    }

    @Test
    void invalidDirection() {
        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                EXISTED_MOVIE_TITLE, YEAR_2012, DIRECTOR_NOLAN, null, 10, 0, ORDER_BY_TITLE, "up"));
        assertEquals(exception.getErrorCode(), INVALID_DIRECTION_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_DIRECTION_MESSAGE);
    }
}
