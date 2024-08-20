package com.example.movie_service.integration;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.helperTool.DataInitializerService;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
public class CustomRepositoryImplIT {

    @SuppressWarnings({"resource"})
    // Since I use @Container here, JUnit will manage the lifecycle of the test
    // container, it also closes the container at the appropriate time, so we don't need to manually handle closing,
    // so we can ignore the warning from the 'MySQLContainer<SELF>' used without 'try'-with-resources statement
    // The try-with-resources  automatically close resources when they are no longer needed. JUnit here does the job
    // for us, so we don't need to use try-with-resources
//    @Container
//    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(SQL_VERSION)
//            .withDatabaseName("testDB")
//            .withUsername("testUser")
//            .withPassword("testPassword")
//            .withReuse(true);
//
//    @DynamicPropertySource
//    static void setUpProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mysqlContainer::getUsername);
//        registry.add("spring.datasource.password", mysqlContainer::getPassword);
//        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
//    }

    // Set up the test class
    @Autowired
    private CustomMovieRepositoryImpl customMovieRepositoryImpl;

    @Autowired
    private DataInitializerService dataInitializerService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void beforeEach() throws SQLException {
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
    void testDatabaseConnection() throws SQLException {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        try (Connection connection = dataSource.getConnection()) {
            String jdbcUrl = connection.getMetaData().getURL();
            System.out.println("Connected to database: " + jdbcUrl);

            // Assert the connection is not null
            assertNotNull(connection);
        }
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
