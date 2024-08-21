package com.example.movie_service.integration;
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

    @BeforeEach
    public void beforeEach() {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.initializeData();
    }

    @AfterEach
    @Transactional
    public void cleanUp() {
        dataInitializerService.clearDatabase();  // Custom method to clear all data
    }

    @Test
    void searchMovieByTitleOnlyFound(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
    }

    @Test
    void searchMovieByTitleNoMovieFound(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Non-existed Movie", null, null, null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    void searchMovieTestAsc(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "releaseTime", "asc");

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
    void searchMovieTestDesc(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "releaseTime", "desc");

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
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                RATING, DESC);

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
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, "Nolan", null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        assertEquals(2, searchResults.size());
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        assertEquals(THE_DARK_KNIGHT, firstResult.getTitle());
        assertEquals(THE_DARK_KNIGHT_RISES, secondResult.getTitle());
    }




}
