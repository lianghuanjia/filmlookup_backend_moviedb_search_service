package com.example.movie_service.moviesearch.integration.service;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import com.example.movie_service.dto.MovieSearchResultWithPaginationDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.moviesearch.integration.util.dataInitService.DataInitializerService;
import com.example.movie_service.moviesearch.integration.util.junitExtension.MySQLTestContainerExtension;
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
class MovieServiceImplIntegrationTests {

    @Autowired
    private MovieServiceImpl movieServiceImpl;

    @Autowired
    private DataInitializerService dataInitializerService;

    private MovieSearchParam movieSearchParam;

    @BeforeEach
    public void beforeEach() {
        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.insertMovieData();
        dataInitializerService.createMovieMaterializedViewTable();

        // Build the basic valid MovieSearchParam
        // NOTE: Because we are passing the movieSearchParam to service layer, so the page is 1-index. Only
        // when the parameters are passed from service layer to repository layer, the page is 0-index.
        movieSearchParam = MovieSearchParam.builder()
                .title(EXISTED_MOVIE_TITLE_LOWER_CASE).releasedYear(null).director(null).genre(null).limit(10).page(1)
                .orderBy(ORDER_BY_TITLE).direction(ASC).build();
    }

    @AfterEach
    public void afterEach() {
        dataInitializerService.clearDatabase();
    }

    @Test
    void testMoviesFoundWithTitleOnly() {
        ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> responseEntity = movieServiceImpl.searchMovies
                (movieSearchParam);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MOVIE_FOUND_CODE, responseEntity.getBody().getCode());
        assertEquals(MOVIE_FOUND_MESSAGE, responseEntity.getBody().getMessage());
        MovieSearchResultWithPaginationDTO dto = responseEntity.getBody().getData();
        // Verify the movies
        assertNotNull(dto);
        List<MovieSearchResponseDTO> movies = dto.getMovies();
        assertEquals(12, dto.getTotalItems());
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_10_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_11_TITLE, movies.get(2).getTitle());
        assertEquals(MOVIE_12_TITLE, movies.get(3).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(4).getTitle());
        // Check if the first movie has overview:
        assertNull(movies.get(0).getOverview());
        assertEquals(MOVIE_1_RELEASE_TIME, movies.get(0).getReleaseTime());
    }

    @Test
    void noMovieFound() {
        movieSearchParam = movieSearchParam.toBuilder()
                .title(NON_EXISTED_MOVIE_TITLE).build();

        ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> responseEntity = movieServiceImpl.searchMovies
                (movieSearchParam);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MOVIE_NOT_FOUND_CODE, responseEntity.getBody().getCode());
        assertEquals(MOVIE_NOT_FOUND_MESSAGE, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    void missingTitle() {
        movieSearchParam = MovieSearchParam.builder()
                .title(EMPTY_STRING).releasedYear(YEAR_2012).director(DIRECTOR_NOLAN).genre(null).limit(10).page(0)
                .orderBy(ORDER_BY_TITLE).direction(ASC).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_TITLE_CODE, exception.getErrorCode());
        assertEquals(INVALID_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void nullTitle() {
        movieSearchParam = movieSearchParam.toBuilder()
                .title(null).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_TITLE_CODE, exception.getErrorCode());
        assertEquals(INVALID_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidYear() {
        movieSearchParam = movieSearchParam.toBuilder()
                .releasedYear(INVALID_YEAR_2030).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_YEAR_CODE, exception.getErrorCode());
        assertEquals(INVALID_YEAR_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidLimit() {
        movieSearchParam = movieSearchParam.toBuilder()
                .limit(5).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_LIMIT_CODE, exception.getErrorCode());
        assertEquals(INVALID_LIMIT_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidOrderBy() {
        movieSearchParam = movieSearchParam.toBuilder()
                .orderBy("director").build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_ORDER_BY_CODE, exception.getErrorCode());
        assertEquals(INVALID_ORDER_BY_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidPage() {
        movieSearchParam = movieSearchParam.toBuilder()
                .page(-1).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_PAGE_CODE, exception.getErrorCode());
        assertEquals(INVALID_PAGE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void invalidDirection() {
        movieSearchParam = movieSearchParam.toBuilder()
                .direction("up").build();

        ValidationException exception = assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(
                movieSearchParam));
        assertEquals(INVALID_DIRECTION_CODE, exception.getErrorCode());
        assertEquals(INVALID_DIRECTION_MESSAGE, exception.getErrorMessage());
    }
}
