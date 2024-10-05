package com.example.movie_service.moviesearch.integration.util.dataInitService;

import com.example.movie_service.entity.movie.Genre;
import com.example.movie_service.entity.movie.Movie;
import com.example.movie_service.entity.movie.MovieCrew;
import com.example.movie_service.entity.movie.MovieRating;
import com.example.movie_service.entity.person.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.movie_service.constants.TestConstant.*;

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
        entityManager.createNativeQuery(DROP_MOVIE_MATERIALIZED_VIEW_QUERY_STRING).executeUpdate();
        entityManager.flush();
    }


    @Transactional()
    public void insertMovieData() {
        List<String> action_list = List.of(ACTION_GENRE);
        List<String> action_crime_list = List.of(ACTION_GENRE, CRIME_GENRE);
        Movie movie1 = initializeOneMovieData(MOVIE_1_TITLE, MOVIE_1_RELEASE_TIME, action_list, DIRECTOR_1, MOVIE_1_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_2_TITLE, MOVIE_2_RELEASE_TIME, action_list, DIRECTOR_1, MOVIE_2_RATING, NUM_OF_VOTES_15, POSTER_PATH);
        initializeOneMovieData(MOVIE_3_TITLE, MOVIE_3_RELEASE_TIME, action_list, DIRECTOR_1, MOVIE_3_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_4_TITLE, MOVIE_4_RELEASE_TIME, action_list, DIRECTOR_2, MOVIE_4_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_5_TITLE, MOVIE_5_RELEASE_TIME, action_list, DIRECTOR_2, MOVIE_5_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_6_TITLE, MOVIE_6_RELEASE_TIME, action_list, DIRECTOR_2, MOVIE_6_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_7_TITLE, MOVIE_7_RELEASE_TIME, action_list, DIRECTOR_2, MOVIE_7_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_8_TITLE, MOVIE_8_RELEASE_TIME, action_list, DIRECTOR_3, MOVIE_8_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_9_TITLE, MOVIE_9_RELEASE_TIME, action_crime_list, DIRECTOR_3, MOVIE_9_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_10_TITLE, MOVIE_10_RELEASE_TIME, action_crime_list, DIRECTOR_3, MOVIE_10_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_11_TITLE, MOVIE_11_RELEASE_TIME, action_crime_list, DIRECTOR_3, MOVIE_11_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_12_TITLE, MOVIE_12_RELEASE_TIME, action_crime_list, DIRECTOR_4, MOVIE_12_RATING, NUM_OF_VOTES_10, POSTER_PATH);
        initializeOneMovieData(MOVIE_13_TITLE, MOVIE_13_RELEASE_TIME, action_crime_list, DIRECTOR_4, MOVIE_13_RATING, NUM_OF_VOTES_10, null);
        // Add 3 crew members to movie 1
        addCrewMemberToMovie(movie1, ACTOR_1_NAME, ACTOR_1_PROFILE_PATH, ACTOR);
        addCrewMemberToMovie(movie1, ACTRESS_1_NAME, ACTRESS_1_PROFILE_PATH, ACTRESS);
        addCrewMemberToMovie(movie1, COMPOSER_1_NAME, COMPOSER_1_PROFILE_PATH, COMPOSER);
    }

    @Transactional
    public void createMovieMaterializedViewTable() {
        entityManager.createNativeQuery(MOVIE_MATERIALIZED_VIEW_QUERY_STRING).executeUpdate();
    }


    @Transactional
    public void addCrewMemberToMovie(Movie movie, String crewName, String crewProfilePath, String jobTitle) {
        Person crewMember = new Person();
        crewMember.setName(crewName);
        crewMember.setProfilePath(crewProfilePath);
        entityManager.persist(crewMember);
        MovieCrew movieCrew = new MovieCrew(movie, crewMember, jobTitle);
        entityManager.persist(movieCrew);
    }

    @Transactional
    public Movie initializeOneMovieData(String title, String releaseTime, List<String> genreNames, String directorName,
                                       Double averageRating, Integer numOfVotes, String posterPath) {
        // Set up genres
        // For each genre name, initialize a Genre, and add it to a Genre set
        Set<Genre> genres = new HashSet<>();
        // For each genre, add it as a Genre entity into the database
        for (String genreName : genreNames) {
            genres.add(createAndPersistGenre(genreName));
        }

        // Set up director Person
        Person director = createAndPersistDirector(directorName);

        // Set up movie
        Movie movie = createAndPersistMovie(title, releaseTime, genres, posterPath);

        // Set up MovieCrew
        createAndPersistMovieCrew(movie, director);

        // Set up rating
        createAndPersistMovieRating(movie, averageRating, numOfVotes);

        return movie;
    }

    private void createAndPersistMovieCrew(Movie movie, Person director) {
        MovieCrew movieCrew = new MovieCrew(movie, director, DIRECTOR_ROLE);
        entityManager.persist(movieCrew);
    }

    private Movie createAndPersistMovie(String title, String releaseTime, Set<Genre> genres, String posterPath) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseTime(releaseTime);
        movie.setGenres(genres);
        movie.setPosterPath(posterPath);
        entityManager.persist(movie);
        return movie;
    }

    private void createAndPersistMovieRating(Movie movie, Double averageRating, Integer numOfVotes) {
        MovieRating movieRating = new MovieRating();
        movieRating.setAverageRating(averageRating);
        movieRating.setNumVotes(numOfVotes);
        movieRating.setMovie(movie);
        entityManager.persist(movieRating);
    }

    private Person createAndPersistDirector(String directorName) {
        // Check if a director with the same name already exists
        Person existingDirector = entityManager.createQuery("SELECT p FROM Person p WHERE p.name = :name", Person.class)
                .setParameter("name", directorName)
                .getResultStream()
                .findFirst()
                .orElse(null);

        // If the director already exists, return it
        if (existingDirector != null) {
            return existingDirector;
        }

        // Otherwise, create and persist a new director
        Person director = new Person();
        director.setName(directorName);
        entityManager.persist(director);
        return director;
    }

    private Genre createAndPersistGenre(String genreName) {
        // Check if a genre with the same name already exists
        Genre existingGenre = entityManager.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                .setParameter("name", genreName)
                .getResultStream()
                .findFirst()
                .orElse(null);

        // If the genre already exists, return it
        if (existingGenre != null) {
            return existingGenre;
        }

        Genre newGenre = new Genre();
        newGenre.setName(genreName);
        entityManager.persist(newGenre);
        return newGenre;
    }
}
