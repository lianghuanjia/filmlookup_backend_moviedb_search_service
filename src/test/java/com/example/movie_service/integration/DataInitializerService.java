package com.example.movie_service.integration;

import com.example.movie_service.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class DataInitializerService {

    @PersistenceContext
    private EntityManager entityManager;

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
    }

    @Transactional
    public void clearDatabase() {
        // When delete table, need to delete the children table first, and then parent. Otherwise, it's going to throw
        // ConstraintViolationException. For example, we need to delete MovieCrew and MovieRating first, and then delete
        // Movie, because they have foreign key movie_id referring to the Movie's movie_id. We can't delete the Movie
        // before deleting the MovieCrew and MovieRating.
        entityManager.createQuery("DELETE FROM MovieCrew").executeUpdate();
        entityManager.createQuery("DELETE FROM MovieRating").executeUpdate();
        entityManager.createQuery("DELETE FROM Movie").executeUpdate();
        entityManager.createQuery("DELETE FROM Person").executeUpdate();
        entityManager.createQuery("DELETE FROM Genre").executeUpdate();
        entityManager.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        // Set up each movie1's rating
        MovieRating movieRating1 = new MovieRating();
        movieRating1.setMovie(movie1);
        movieRating1.setAverageRating(9.5);
        movieRating1.setNumVotes(10);
        entityManager.persist(movieRating1);

        // Set up each movie2's rating
        MovieRating movieRating2 = new MovieRating();
        movieRating2.setMovie(movie2);
        movieRating2.setAverageRating(9.0);
        movieRating2.setNumVotes(10);
        entityManager.persist(movieRating2);

        // Set up each movie3's rating
        MovieRating movieRating3 = new MovieRating();
        movieRating3.setMovie(movie3);
        movieRating3.setAverageRating(8.5);
        movieRating3.setNumVotes(10);
        entityManager.persist(movieRating3);
    }
}
