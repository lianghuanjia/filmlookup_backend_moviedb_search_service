package com.example.movie_service.integration;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Genre;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.entity.MovieCrew;
import com.example.movie_service.entity.Person;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomRepositoryImplIT {

    private static String SQL_VERSION = "mysql:8.0.39";

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(SQL_VERSION)
            .withDatabaseName("testDB")
            .withUsername("testUser")
            .withPassword("testPassword")
            .withReuse(true);

    // Set up EntityManager
    @PersistenceContext
    private EntityManager entityManager;


    // Set up the test class
    @Autowired
    private CustomMovieRepositoryImpl customMovieRepositoryImpl;

    @DynamicPropertySource
    static void setUpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    @Transactional
    public void checkDatabaseEmpty() {
        // Check if the database is empty
        long movieCount = (long) entityManager.createQuery("SELECT COUNT(m) FROM Movie m").getSingleResult();
        long genreCount = (long) entityManager.createQuery("SELECT COUNT(g) FROM Genre g").getSingleResult();
        long personCount = (long) entityManager.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
        long movieCrewCount = (long) entityManager.createQuery("SELECT COUNT(mc) FROM MovieCrew mc").getSingleResult();

        if (movieCount > 0 || genreCount > 0 || personCount > 0 || movieCrewCount > 0) {
            throw new IllegalStateException("Database is not empty before the test runs.");
        }

        entityManager.clear();
    }

    @BeforeEach
    @Transactional
    public void setUp() {
        // Set up a Genre
        Genre genre = new Genre();
//        genre.setId(1); Do not set an ID before you save/persist it. That's the only problem here. Hibernate looks at
//        the Entity you've passed in and assumes that because it has its PK populated that it is already in the database.
        genre.setName("Action");
        entityManager.persist(genre);

        // Set up a director Person
        Person director = new Person();
        director.setName("Christopher Nolan");
//        director.setId("nm1");
        entityManager.persist(director);

        // Set up a Movie
        Movie movie = new Movie();
//        movie.setId("tt1");
        movie.setTitle("The Dark Knight");
        movie.setBackdropPath("backdropPath");
        movie.setPosterPath("posterPath");
        movie.setReleaseTime("2008-05-08");
        entityManager.persist(movie);

        // Add the genre to the movie
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        movie.setGenres(genres);
        entityManager.persist(movie);

        // Create a MovieCrew
        MovieCrew movieCrew = new MovieCrew(movie, director, "director");
        entityManager.persist(movieCrew);
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
//        MovieSearchResultDTO firstResult = searchResults.get(0);
//        assertEquals(firstResult.getTitle(), "The Dark Knight");
    }




}
