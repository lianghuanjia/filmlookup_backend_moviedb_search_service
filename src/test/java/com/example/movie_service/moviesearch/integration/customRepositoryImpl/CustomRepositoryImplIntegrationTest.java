package com.example.movie_service.moviesearch.integration.customRepositoryImpl;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchWithTitleDTOFromRepoToService;
import com.example.movie_service.dto.MovieTitleSearchSQLQueryResultDTO;
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

    @BeforeEach
    public void beforeEach() {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.insertMovieData();
        dataInitializerService.createMovieMaterializedViewTable();

        // Build the basic valid MovieSearchParam
        movieSearchParam = MovieSearchParam.builder()
                .title(EXISTED_MOVIE_TITLE_LOWER_CASE).releasedYear(null).director(null).genre(null).limit(10).page(0)
                .orderBy(ORDER_BY_TITLE).direction(ASC).build();
    }

    @AfterEach
    @Transactional
    public void cleanUp() {
        dataInitializerService.clearDatabase();  // Custom method to clear all data
    }

    @Test
    void searchMovieByTitleOnly_FoundMovie() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).build();

        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        assertEquals(12, totalItem);
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_10_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_11_TITLE, movies.get(2).getTitle());
        assertEquals(MOVIE_12_TITLE, movies.get(3).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(4).getTitle());
    }

    @Test
    void searchMovieByTitleOnly_WithSubstringTitle_MovieFound() {
        movieSearchParam = movieSearchParam.toBuilder().title("vie").build();

        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        assertEquals(12, totalItem);
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_10_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_11_TITLE, movies.get(2).getTitle());
        assertEquals(MOVIE_12_TITLE, movies.get(3).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(4).getTitle());
    }

    @Test
    void searchMovieByTitleNoMovieFound() {
        movieSearchParam = movieSearchParam.toBuilder().title(NON_EXISTED_MOVIE_TITLE).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();

        assertEquals(0, totalItem);
        assertTrue(movies.isEmpty());
    }

    @Test
    void searchMovieTest_OrderByReleaseTime_Asc() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).orderBy(RELEASE_TIME).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        assertEquals(12, totalItem);
        assertEquals(10, movies.size());
        assertEquals(MOVIE_11_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_10_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_9_TITLE, movies.get(2).getTitle());
    }

    @Test
    void searchMovieTest_OrderByReleaseTime_Asc_SecondPage() {
        //Below we want to go to page 2, so we set the page to be 1, because the test runs at repository layer, so page is 0-index.
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).orderBy(RELEASE_TIME).page(1).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        for (MovieTitleSearchSQLQueryResultDTO movie : movies) {
            System.out.println(movie);
        }
        assertEquals(12, totalItem);
        assertEquals(2, movies.size());
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_12_TITLE, movies.get(1).getTitle());
    }

    @Test
    void searchMovieTestOrderByReleaseTimeDesc() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).orderBy(RELEASE_TIME).direction(DESC).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);
        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        assertEquals(12, totalItem);
        assertEquals(10, movies.size());
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_3_TITLE, movies.get(2).getTitle());
        assertEquals(MOVIE_4_TITLE, movies.get(3).getTitle());
        assertEquals(MOVIE_5_TITLE, movies.get(4).getTitle());
    }

    @Test
    void searchMovieTest_OrderByRating_Desc() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).orderBy(RATING).direction(DESC).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);
        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();
        assertEquals(12, totalItem);
        assertEquals(10, movies.size());
        assertEquals(MOVIE_12_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_11_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_10_TITLE, movies.get(2).getTitle());
    }

    @Test
    void searchMovieTest_OrderByRating_ASC() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).orderBy(RATING).direction(ASC).build();
        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();

        assertEquals(12, totalItem);
        assertEquals(10, movies.size());
        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_3_TITLE, movies.get(2).getTitle());
    }

    @Test
    void searchMovieByTitleAndDirector() {
        movieSearchParam = movieSearchParam.toBuilder().title(EXISTED_MOVIE_TITLE_LOWER_CASE).director(DIRECTOR_1).build();

        MovieSearchWithTitleDTOFromRepoToService searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        int totalItem = searchResults.getTotalItem();
        List<MovieTitleSearchSQLQueryResultDTO> movies = searchResults.getMovies();

        assertEquals(3, totalItem);
        assertEquals(3, movies.size());

        assertEquals(MOVIE_1_TITLE, movies.get(0).getTitle());
        assertEquals(MOVIE_2_TITLE, movies.get(1).getTitle());
        assertEquals(MOVIE_3_TITLE, movies.get(2).getTitle());
    }
}
