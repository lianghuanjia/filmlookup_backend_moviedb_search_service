package com.example.movie_service.moviesearch.integration.customRepositoryImpl;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.moviesearch.integration.util.dataInitService.DataInitializerService;
import com.example.movie_service.moviesearch.integration.util.junitExtension.MySQLTestContainerExtension;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.movie_service.constants.TestConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomRepositoryImplIntegrationTest {

    // Set up the test class
    @Autowired
    private CustomMovieRepositoryImpl customMovieRepositoryImpl;

    @Autowired
    private DataInitializerService dataInitializerService;

    private MovieSearchParam movieSearchParam;

    private static final String DARK_KNIGHT = "Dark Knight";

    @BeforeEach
    public void beforeEach() {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.initializeData();

        // Build the basic valid MovieSearchParam
        movieSearchParam = MovieSearchParam.builder()
                .title(EXISTED_MOVIE_TITLE).releasedYear(null).director(null).genre(null).limit(10).page(0)
                .orderBy(ORDER_BY_TITLE).direction(ASC).build();
    }

    @AfterEach
    @Transactional
    public void cleanUp() {
        dataInitializerService.clearDatabase();  // Custom method to clear all data
    }

    @Test
    void searchMovieByTitleOnlyFound() {
        movieSearchParam = movieSearchParam.toBuilder().title(THE_DARK_KNIGHT).build();

        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_OVERVIEW, firstResult.getOverview());
    }

    @Test
    void searchMovieByTitleOnly_WithSubstringTitle_MovieFound() {
        movieSearchParam = movieSearchParam.toBuilder().title(DARK_KNIGHT).build();

        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_OVERVIEW, firstResult.getOverview());
    }

    @Test
    void searchMovieByTitleNoMovieFound() {
        movieSearchParam = movieSearchParam.toBuilder().title(NON_EXISTED_MOVIE_TITLE).build();
        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    void searchMovieTestOrderByReleaseTimeAsc() {
        movieSearchParam = movieSearchParam.toBuilder().title(THE_DARK_KNIGHT).orderBy(RELEASE_TIME).build();
        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        MovieSearchQueryDTO secondResult = searchResults.get(1);
        MovieSearchQueryDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, thirdResult.getTitle());
    }

    @Test
    void searchMovieTestOrderByReleaseTimeDesc() {
        movieSearchParam = movieSearchParam.toBuilder().title(THE_DARK_KNIGHT).orderBy(RELEASE_TIME).direction(DESC).build();
        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        MovieSearchQueryDTO secondResult = searchResults.get(1);
        MovieSearchQueryDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT, thirdResult.getTitle());
    }

    @Test
    void searchMovieTestDescAndOrderByRating() {
        movieSearchParam = movieSearchParam.toBuilder().title(THE_DARK_KNIGHT).orderBy(RATING).direction(DESC).build();
        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        MovieSearchQueryDTO secondResult = searchResults.get(1);
        MovieSearchQueryDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, thirdResult.getTitle());
    }

    @Test
    void searchMovieByTitleAndDirector() {
        movieSearchParam = movieSearchParam.toBuilder().title(THE_DARK_KNIGHT).director("Nolan").build();

        List<MovieSearchQueryDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);


        assertNotNull(searchResults);
        assertEquals(2, searchResults.size());
        MovieSearchQueryDTO firstResult = searchResults.get(0);
        MovieSearchQueryDTO secondResult = searchResults.get(1);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
    }
}
