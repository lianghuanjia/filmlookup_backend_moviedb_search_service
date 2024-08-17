package com.example.movie_service.integration;


import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.response.CustomResponse;
import lombok.ToString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIT {


    private static final String SQL_VERSION = "mysql:8.0.39";
    private static final String EXISTED_MOVIE_TITLE = "Knight";
    private static final String NON_EXISTED_MOVIE_TITLE = "Miss Jerry";
    private static final String TITLE = "title";
    private static final String RELEASED_YEAR = "releasedYear";
    private static final String ORDER_BY = "orderBy";
    private static final String DIRECTION = "direction";

    @SuppressWarnings({"resource"})
    // "resource":
    // Since I use @Container here, JUnit will manage the lifecycle of the test
    // container, it also closes the container at the appropriate time, so we don't need to manually handle closing,
    // so we can ignore the warning from the 'MySQLContainer<SELF>' used without 'try'-with-resources statement
    // The try-with-resources  automatically close resources when they are no longer needed. JUnit here does the job
    // for us, so we don't need to use try-with-resources
    // "unused":
    // The mysqlContainer is used because in the setUp(), the database url has the mysqlContainer database name,
    // so the mysqlContainer is actually used.
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
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataInitializerService dataInitializerService;

    // Get the random port generated by @SpringBootTest
    @LocalServerPort
    private int port;

    private static final String protocolAndHost = "http://localhost:";
    private String searchMoviePath;
    // Define the response type using ParameterizedTypeReference
    private final ParameterizedTypeReference<CustomResponse<List<MovieSearchResultDTO>>> responseType =
            new ParameterizedTypeReference<CustomResponse<List<MovieSearchResultDTO>>>() {};

    @BeforeEach
    public void setUp(){
        String baseUrl = protocolAndHost + port + "/v1/api";
        searchMoviePath = baseUrl + "/movies";

        dataInitializerService.checkDatabaseEmpty();
        dataInitializerService.initializeData();
    }

    @AfterEach
    public void afterEach() {
        // Clean the database
        dataInitializerService.clearDatabase();
    }

    @Test
    public void testDatabaseConnection() throws SQLException {
        // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to database: " + connection.getMetaData().getURL());
        }
    }


    @Test
    void testSearchMoviesMovieFound() {
        // Define URI with query param
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert
        assertTrue(results.getStatusCode().is2xxSuccessful());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);
        // Assert that the return code is 20001, representing movies found
        assertEquals(customResponse.getCode(), MOVIE_FOUND_CODE);
        assertEquals(customResponse.getMessage(), MOVIE_FOUND_MESSAGE);
        List<MovieSearchResultDTO> movieList = customResponse.getData();
        // Assert there are 3 movies
        assertEquals(movieList.size(), 3);
        // Assert 1st movie is The Dark Knight
        assertEquals(movieList.get(0).getTitle(), "The Dark Knight");
        assertEquals(movieList.get(1).getTitle(), "The Dark Knight Rises");
        assertEquals(movieList.get(2).getTitle(), "The Dark Knight Rises Again");
    }

    @Test
    void testSearchMoviesNotFound() {
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, NON_EXISTED_MOVIE_TITLE)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert the status code is OK, and we have a customResponse
        assertTrue(results.getStatusCode().is2xxSuccessful());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);

        // Assert the code and message are MOVIE_NOT_FOUND
        assertEquals(customResponse.getCode(), MOVIE_NOT_FOUND_CODE);
        assertEquals(customResponse.getMessage(), MOVIE_NOT_FOUND_MESSAGE);

        // Assert the MovieSearchResultDTO List is empty
        assertTrue(customResponse.getData().isEmpty());
    }

    @Test
    void SearchMoviesNoTitle() {
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert the status code is OK, and we have a customResponse
        assertTrue(results.getStatusCode().is4xxClientError());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);

        // Assert the code and message are MOVIE_NOT_FOUND
        assertEquals(customResponse.getCode(), MISSING_TITLE_CODE);
        assertEquals(customResponse.getMessage(), MISSING_TITLE_MESSAGE);

        // Assert the MovieSearchResultDTO List is empty
        assertNull(customResponse.getData());
    }

    @Test
    void testSearchMoviesValidYear() {
        String validYear = "2012";
        // Define URI with query param
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .queryParam(RELEASED_YEAR, validYear)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert
        assertTrue(results.getStatusCode().is2xxSuccessful());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);
        // Assert that the return code is MOVIE_FOUND_CODE, representing movies found
        assertEquals(customResponse.getCode(), MOVIE_FOUND_CODE);
        assertEquals(customResponse.getMessage(), MOVIE_FOUND_MESSAGE);
        List<MovieSearchResultDTO> movieList = customResponse.getData();
        // Assert there are 2 movies with release time in 2012.
        assertEquals(movieList.size(), 2);
        // Assert 1st movie is The Dark Knight Rises, and the second one is The Dark Knight Rises Again,
        // because we didn't put orderBy and direction in the query param, so it sets to default with
        // orderBy = "title", direction = "asc".
        assertEquals(movieList.get(0).getTitle(), "The Dark Knight Rises");
        assertEquals(movieList.get(1).getTitle(), "The Dark Knight Rises Again");
    }


    @Test
    void SearchMoviesInvalidReleasedYear() {
        String invalidYear = "2025";
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .queryParam(RELEASED_YEAR, invalidYear)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert the status code is BAD_REQUEST, and we have a customResponse
        assertTrue(results.getStatusCode().is4xxClientError());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);

        // Assert the code and message are INVALID_YEAR
        assertEquals(customResponse.getCode(), INVALID_YEAR_CODE);
        assertEquals(customResponse.getMessage(), INVALID_YEAR_MESSAGE);

        // Assert the data in customResponse is null
        assertNull(customResponse.getData());
    }

    @Test
    void searchMoviesValidWithReleaseTimeOrderByAndDescDirection() {
        String direction = "desc";
        String orderBy = "releaseTime";
        // Define URI with query param
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .queryParam(ORDER_BY, orderBy)
                .queryParam(DIRECTION, direction)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert
        assertTrue(results.getStatusCode().is2xxSuccessful());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);
        // Assert that the return code is MOVIE_FOUND_CODE, representing movies found
        assertEquals(customResponse.getCode(), MOVIE_FOUND_CODE);
        assertEquals(customResponse.getMessage(), MOVIE_FOUND_MESSAGE);
        List<MovieSearchResultDTO> movieList = customResponse.getData();
        // Assert there are 3 movies
        assertEquals(movieList.size(), 3);
        // Assert 1st movie is The Dark Knight Rises Again, since it has the latest release time
        assertEquals(movieList.get(0).getTitle(), "The Dark Knight Rises Again");
        assertEquals(movieList.get(1).getTitle(), "The Dark Knight Rises");
        assertEquals(movieList.get(2).getTitle(), "The Dark Knight");
    }

    @Test
    void SearchMoviesInvalidOrderBy() {
        String invalidOrderBy = "director";
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .queryParam(ORDER_BY, invalidOrderBy)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert the status code is BAD_REQUEST, and we have a customResponse
        assertTrue(results.getStatusCode().is4xxClientError());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);

        // Assert the code and message are INVALID_ORDER_BY
        assertEquals(customResponse.getCode(), INVALID_ORDER_BY_CODE);
        assertEquals(customResponse.getMessage(), INVALID_ORDER_BY_MESSAGE);

        // Assert the data in customResponse is null
        assertNull(customResponse.getData());
    }

    @Test
    void SearchMoviesInvalidDirection() {
        String invalidDirection = "up";
        URI uri = UriComponentsBuilder.fromHttpUrl(searchMoviePath)
                .queryParam(TITLE, EXISTED_MOVIE_TITLE)
                .queryParam(DIRECTION, invalidDirection)
                .build().toUri();

        // Perform a GET request to the controller
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> results = restTemplate.exchange
                (uri, HttpMethod.GET, null, responseType);

        // Assert the status code is BAD_REQUEST, and we have a customResponse
        assertTrue(results.getStatusCode().is4xxClientError());
        CustomResponse<List<MovieSearchResultDTO>> customResponse = results.getBody();
        assertNotNull(customResponse);

        // Assert the code and message are INVALID_YEAR
        assertEquals(customResponse.getCode(), INVALID_DIRECTION_CODE);
        assertEquals(customResponse.getMessage(), INVALID_DIRECTION_MESSAGE);

        // Assert the data in customResponse is null
        assertNull(customResponse.getData());
    }

}
