package com.example.movie_service.repository;

import com.example.movie_service.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Transactional
public class CustomMovieRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CustomMovieRepositoryImpl customMovieRepositoryImpl;

    @BeforeEach
    void setUp() {
        // Insert reusable test data into the in-memory database
        entityManager.persist(new Movie(
                "tt0468569", // String id
                "Movie 1", // String title
                "Movie 1", // String originalTitle
                Byte.valueOf((byte) 0), // Byte isAdult
                "2008-07-16", // String releaseTime
                Integer.valueOf(2008), // Integer endYear
                Integer.valueOf(152), // Integer runtimeMinutes
                "backdrop1.jpg", // String backdropPath
                "posterPath1", // String posterPath
                "Batman raises the stakes in his war on crime.", // String overview
                "Welcome to a world without rules.", // String tagline
                Long.valueOf(1004558444L), // Long revenue
                "Released", // String status
                Long.valueOf(185000000L), // Long budget
                null, // Set<Genre> genres
                null, // Set<MovieCrew> movieCrews
                null, // Set<MovieAkas> movieAkas
                null // MovieRating movieRating
        ));

        entityManager.persist(new Movie(
                "tt1345836", // String id
                "Movie 2", // String title
                "Movie 2", // String originalTitle
                Byte.valueOf((byte) 0), // Byte isAdult
                "2012-07-17", // String releaseTime
                null, // Integer endYear
                Integer.valueOf(164), // Integer runtimeMinutes
                "backdrop2.jpg", // String backdropPath
                "posterPath2", // String posterPath
                "Following the death of District Attorney Harvey Dent.", // String overview
                "A fire will rise.", // String tagline
                Long.valueOf(1081041287L), // Long revenue
                "Released", // String status
                Long.valueOf(250000000), // Long budget
                null, // Set<Genre> genres
                null, // Set<MovieCrew> movieCrews
                null, // Set<MovieAkas> movieAkas
                null // MovieRating movieRating
        ));

        // Flush and clear to ensure data is persisted
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testSearchMoviesFoundWithTitleOnly() {
        // Run the method under test with ASC order
        List<Object[]> resultsAsc = customMovieRepositoryImpl.searchMovies("Movie", null, null, null, 10, 0, "m.primaryTitle", "ASC");

        // Validate that results are returned in ascending order
        assertEquals(2, resultsAsc.size());  // Ensure we got the expected number of results
        assertEquals("Movie 1", resultsAsc.get(0)[1]);
        assertEquals("Movie 2", resultsAsc.get(1)[1]);
    }


}
