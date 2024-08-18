package com.example.movie_service.integration;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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

import static com.example.movie_service.constants.TestConstant.SQL_VERSION;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@Testcontainers
// disables the web layer, making this configuration more suitable for testing repositories.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomRepositoryImplIT {

    @SuppressWarnings({"resource"})
    // Since I use @Container here, JUnit will manage the lifecycle of the test
    // container, it also closes the container at the appropriate time, so we don't need to manually handle closing,
    // so we can ignore the warning from the 'MySQLContainer<SELF>' used without 'try'-with-resources statement
    // The try-with-resources  automatically close resources when they are no longer needed. JUnit here does the job
    // for us, so we don't need to use try-with-resources
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
    public void testDatabaseConnection() throws SQLException {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to database: " + connection.getMetaData().getURL());
        }
    }

    @Test
    public void searchMovieByTitleOnlyFound(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        assertEquals(firstResult.getTitle(), "The Dark Knight");
    }

    @Test
    public void searchMovieByTitleNoMovieFound(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Non-existed Movie", null, null, null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void searchMovieTestAsc(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "releaseTime", "asc");

        assertNotNull(searchResults);
        assertEquals(searchResults.size(), 3);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(firstResult.getTitle(), "The Dark Knight");
        assertEquals(secondResult.getTitle(), "The Dark Knight Rises");
        assertEquals(thirdResult.getTitle(), "The Dark Knight Rises Again");
    }

    @Test
    public void searchMovieTestDesc(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "releaseTime", "desc");

        assertNotNull(searchResults);
        assertEquals(searchResults.size(), 3);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(firstResult.getTitle(), "The Dark Knight Rises Again");
        assertEquals(secondResult.getTitle(), "The Dark Knight Rises");
        assertEquals(thirdResult.getTitle(), "The Dark Knight");
    }

    @Test
    public void searchMovieTestDescAndOrderByRating(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, null, null, 10, 0,
                "rating", "desc");

        assertNotNull(searchResults);
        assertEquals(searchResults.size(), 3);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        MovieSearchResultDTO thirdResult = searchResults.get(2);
        assertEquals(firstResult.getTitle(), "The Dark Knight");
        assertEquals(secondResult.getTitle(), "The Dark Knight Rises");
        assertEquals(thirdResult.getTitle(), "The Dark Knight Rises Again");
    }

    @Test
    public void searchMovieByTitleAndDirector(){
        List<MovieSearchResultDTO> searchResults = customMovieRepositoryImpl.searchMovies(
                "Dark Knight", null, "Nolan", null, 10, 0,
                "title", "asc");


        assertNotNull(searchResults);
        assertEquals(searchResults.size(), 2);
        MovieSearchResultDTO firstResult = searchResults.get(0);
        MovieSearchResultDTO secondResult = searchResults.get(1);
        assertEquals(firstResult.getTitle(), "The Dark Knight");
        assertEquals(secondResult.getTitle(), "The Dark Knight Rises");
    }




}
