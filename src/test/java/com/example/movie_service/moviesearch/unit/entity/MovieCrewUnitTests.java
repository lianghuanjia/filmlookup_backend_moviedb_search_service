package com.example.movie_service.moviesearch.unit.entity;

import com.example.movie_service.entity.movie.Movie;
import com.example.movie_service.entity.movie.MovieCrew;
import com.example.movie_service.entity.movie.MovieCrewId;
import com.example.movie_service.entity.person.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MovieCrewUnitTests {

    @Test
    void testMovieCrewConstructor() {
        Movie movie = new Movie();
        movie.setId("movie1");
        Person person = new Person();
        person.setId("person1");
        String job = "director";
        // Create an instance of MovieCrew using the constructor
        MovieCrew movieCrew = new MovieCrew(movie, person, job);

        // Verify that the movie, person, and job fields are set correctly
        assertEquals(movie, movieCrew.getMovie());
        assertEquals(person, movieCrew.getPerson());
        assertEquals(job, movieCrew.getJob());
        assertEquals("movie1", movieCrew.getMovie().getId());
        assertEquals("person1", movieCrew.getPerson().getId());
        assertEquals(job, movieCrew.getJob());

        // Verify that the id field (composite key) is set correctly
        MovieCrewId expectedId = new MovieCrewId(movie.getId(), person.getId(), job);
        assertNotNull(movieCrew.getId());
        assertEquals(expectedId.getMovieId(), movieCrew.getId().getMovieId());
        assertEquals(expectedId.getPersonId(), movieCrew.getId().getPersonId());
        assertEquals(expectedId.getJob(), movieCrew.getId().getJob());
    }
}
