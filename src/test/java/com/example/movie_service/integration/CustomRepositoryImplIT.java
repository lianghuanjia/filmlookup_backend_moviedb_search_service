package com.example.movie_service.integration;
import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.dataInitService.DataInitializerService;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.movie_service.constants.TestConstant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
// @Testcontainers
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest
// (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomRepositoryImplIT {

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
    void searchMovieByTitleOnlyFound(){
        movieSearchParam = movieSearchParam.toBuilder().title("Dark Knight").build();

        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
    }

    @Test
    void searchMovieByTitleNoMovieFound(){
        movieSearchParam = movieSearchParam.toBuilder().title(NON_EXISTED_MOVIE_TITLE).build();
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);


        assertNotNull(searchResults);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    void searchMovieTestOrderByReleaseTimeAsc(){
        movieSearchParam = movieSearchParam.toBuilder().title("Dark Knight").orderBy(RELEASE_TIME).build();
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, thirdResult.getTitle());
    }

    @Test
    void searchMovieTestOrderByReleaseTimeDesc(){
        movieSearchParam = movieSearchParam.toBuilder().title("Dark Knight").orderBy(RELEASE_TIME).direction(DESC).build();
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT, thirdResult.getTitle());
    }

    @Test
    void searchMovieTestDescAndOrderByRating(){
        movieSearchParam = movieSearchParam.toBuilder().title("Dark Knight").orderBy(RATING).direction(DESC).build();
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        assertNotNull(searchResults);
        assertEquals(3, searchResults.size());
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES_AGAIN, thirdResult.getTitle());
    }

    @Test
    void searchMovieByTitleAndDirector(){
        movieSearchParam = movieSearchParam.toBuilder().title("Dark Knight").director("Nolan").build();

        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(movieSearchParam);


        assertNotNull(searchResults);
        assertEquals(2, searchResults.size());
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
    }




}
