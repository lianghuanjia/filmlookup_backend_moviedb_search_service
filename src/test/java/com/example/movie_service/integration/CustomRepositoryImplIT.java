package com.example.movie_service.integration;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Genre;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.entity.MovieCrew;
import com.example.movie_service.entity.Person;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
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
    public void beforeEach() {
        checkDatabaseEmpty();
        initializeData();
    }

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

    @Transactional
    public void initializeData() {
        // Set up a Genre
        Genre genre = new Genre();
//        genre.setId(1); Do not set an ID before you save/persist it. That's the only problem here. Hibernate looks at
//        the Entity you've passed in and assumes that because it has its PK populated that it is already in the database.
        genre.setName("Action");
        entityManager.persist(genre);

        // Set up a director Person
        Person director = new Person();
        director.setName("Christopher Nolan");
        entityManager.persist(director);

        // Set up a Movie 1
        Movie movie1 = new Movie();
        movie1.setTitle("The Dark Knight");
        movie1.setReleaseTime("2008-05-08");
        entityManager.persist(movie1);

        // Set up a Movie 2
        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight Rises");
        movie2.setReleaseTime("2012-03-09");
        entityManager.persist(movie2);

        // Add the genre to the movie
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        movie1.setGenres(genres);
        movie2.setGenres(genres);
        entityManager.persist(movie1);
        entityManager.persist(movie2);

        // Set up MovieCrew
        MovieCrew movieCrew1 = new MovieCrew(movie1, director, "director");
        entityManager.persist(movieCrew1);
        MovieCrew movieCrew2 = new MovieCrew(movie2, director, "director");
        entityManager.persist(movieCrew2);

        // Set up a Movie 3
        Movie movie3 = new Movie();
        movie3.setTitle("The Dark Knight Rises Again");
        movie3.setReleaseTime("2012-08-09");
        entityManager.persist(movie3);

        // Set up Movie 3 genre, director, and movie crew
        Genre genre2 = new Genre();
        genre2.setName("Love");
        entityManager.persist(genre2);
        Set<Genre> genreSet2 = new HashSet<>();
        genreSet2.add(genre2);
        movie3.setGenres(genreSet2);
        entityManager.persist(movie3);

        Person director2 = new Person();
        director2.setName("Third director");
        entityManager.persist(director2);

        MovieCrew movieCrew3 = new MovieCrew(movie3, director2, "director");
        entityManager.persist(movieCrew3);
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
