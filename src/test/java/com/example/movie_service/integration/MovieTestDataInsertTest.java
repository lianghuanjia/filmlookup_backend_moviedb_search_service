package com.example.movie_service.integration;

import com.example.movie_service.entity.Genre;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.entity.MovieCrew;
import com.example.movie_service.entity.MovieRating;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static com.example.movie_service.constants.TestConstant.*;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieTestDataInsertTest {

    @Autowired
    private DataInitializerService dataInitializerService;

    @PersistenceContext
    private EntityManager entityManager;

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

    // Need @Transactional because I am fetching genres data from a movie, and the fetching is lazy loading.
    // @Transactional ensures that the database session (or Hibernate session) remains open and active for the duration
    // of the method's execution.
    @Transactional
    @Test
    public void testInsertMovieData() {
        List<String> action_list = List.of(ACTION_GENRE);
        List<String> action_crime_list = List.of(ACTION_GENRE, CRIME_GENRE);
        dataInitializerService.initializeOneMovieData(MOVIE_1_TITLE, MOVIE_1_RELEASE_TIME, action_list, DIRECTOR_1, MOVIE_1_RATING, NUM_OF_VOTES_10);
        dataInitializerService.initializeOneMovieData(MOVIE_2_TITLE, MOVIE_2_RELEASE_TIME, action_crime_list, DIRECTOR_2, MOVIE_2_RATING, NUM_OF_VOTES_15);

        // Assert: Verify that the movies have been created
        List<Movie> movies = entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        assertEquals(2, movies.size());

        // Verify the first movie's properties
        Movie movie1 = movies.stream().filter(m -> m.getTitle().equals(MOVIE_1_TITLE)).findFirst().orElse(null);
        assertNotNull(movie1);
        assertEquals(MOVIE_1_TITLE, movie1.getTitle());
        assertEquals(MOVIE_1_RELEASE_TIME, movie1.getReleaseTime());

        // Verify the director of the first movie
        MovieCrew movieCrew1 = entityManager.createQuery("SELECT mc FROM MovieCrew mc WHERE mc.movie = :movie", MovieCrew.class)
                .setParameter("movie", movie1)
                .getSingleResult();
        assertNotNull(movieCrew1);
        assertEquals(DIRECTOR_1, movieCrew1.getPerson().getName());
        assertEquals(DIRECTOR_ROLE, movieCrew1.getJob());

        // Verify the ratings of the first movie
        MovieRating ratingMovie1 = entityManager.createQuery("SELECT r FROM MovieRating r WHERE r.movie = :movie", MovieRating.class)
                .setParameter("movie", movie1)
                .getSingleResult();
        assertNotNull(ratingMovie1);
        assertEquals(MOVIE_1_RATING, ratingMovie1.getAverageRating());
        assertEquals(NUM_OF_VOTES_10, ratingMovie1.getNumVotes());

        // Verify the release year of movie1
        assertEquals(movie1.getReleaseTime(), MOVIE_1_RELEASE_TIME);

        // Verify the genre of the first movie
        Set<Genre> genresMovie1 = movie1.getGenres();
        assertEquals(1, genresMovie1.size());
        assertTrue(genresMovie1.stream().anyMatch(g -> g.getName().equals(ACTION_GENRE)));


        // Get movie2
        Movie movie2 = movies.stream().filter(m -> m.getTitle().equals(MOVIE_2_TITLE)).findFirst().orElse(null);
        assertNotNull(movie2);
        assertEquals(MOVIE_2_TITLE, movie2.getTitle());
        assertEquals(MOVIE_2_RELEASE_TIME, movie2.getReleaseTime());

        // Verify the genres of the second movie is ACTION_GENRE and CRIME_GENRE
        Set<Genre> genresMovie2 = movie2.getGenres();
        assertEquals(2, genresMovie2.size());
        assertTrue(genresMovie2.stream().anyMatch(g -> g.getName().equals(ACTION_GENRE)));
        assertTrue(genresMovie2.stream().anyMatch(g -> g.getName().equals(CRIME_GENRE)));
    }


}
