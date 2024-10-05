package com.example.movie_service.moviesearch.unit.converter;

import com.example.movie_service.converter.MovieSearchQueryToResponseConverter;
import com.example.movie_service.dto.MovieTitleSearchSQLQueryResultDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.movie_service.constant.MovieConstant.DEFAULT_POSTER_PATH;
import static com.example.movie_service.constant.MovieConstant.DEFAULT_RATING_SCORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieSearchQueryToResponseConverterUnitTests {

    private MovieSearchQueryToResponseConverter converter;
    private static final String id = "1";
    private static final String title = "Inception";
    private static final String releaseTime = "2010";
    private static final String director = "Christopher Nolan";
    private static final String backdropPath = "path/to/backdrop";
    private static final String posterPath = "path/to/poster";
    private static final String overview = "overview";
    private static final Double rating = 9.0;

    @BeforeEach
    void setUp() {
        converter = new MovieSearchQueryToResponseConverter();
    }

    @Test
    void testConvertWithAllFields() {
        // Given
        MovieTitleSearchSQLQueryResultDTO queryDTO = new MovieTitleSearchSQLQueryResultDTO(id, title, releaseTime, director, backdropPath, posterPath, rating, overview);

        // When
        MovieSearchResponseDTO responseDTO = converter.convert(queryDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals(id, responseDTO.getId());
        assertEquals(title, responseDTO.getTitle());
        assertEquals(releaseTime, responseDTO.getReleaseTime());
        assertEquals(posterPath, responseDTO.getPosterPath());
        assertEquals(rating, responseDTO.getRating());
    }

    @Test
    void testConvertWithNullPosterPath() {
        // Given
        MovieTitleSearchSQLQueryResultDTO queryDTO = new MovieTitleSearchSQLQueryResultDTO(id, title, releaseTime, director, backdropPath, null, rating, overview);

        // When
        MovieSearchResponseDTO responseDTO = converter.convert(queryDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals(id, responseDTO.getId());
        assertEquals(title, responseDTO.getTitle());
        assertEquals(releaseTime, responseDTO.getReleaseTime());
        assertEquals(DEFAULT_POSTER_PATH, responseDTO.getPosterPath());
        assertEquals(rating, responseDTO.getRating());
    }

    @Test
    void testConvertWithNullRating() {
        // Given
        MovieTitleSearchSQLQueryResultDTO queryDTO = new MovieTitleSearchSQLQueryResultDTO(id, title, releaseTime, director, backdropPath, posterPath, null, overview);

        // When
        MovieSearchResponseDTO responseDTO = converter.convert(queryDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals(id, responseDTO.getId());
        assertEquals(title, responseDTO.getTitle());
        assertEquals(releaseTime, responseDTO.getReleaseTime());
        assertEquals(posterPath, responseDTO.getPosterPath());
        assertEquals(DEFAULT_RATING_SCORE, responseDTO.getRating());
    }

    @Test
    void testConvertWithAllNullFields() {
        // Given
        MovieTitleSearchSQLQueryResultDTO queryDTO = new MovieTitleSearchSQLQueryResultDTO(null, null, null, null, null, null, null, null);

        // When
        MovieSearchResponseDTO responseDTO = converter.convert(queryDTO);

        // Then
        assertNotNull(responseDTO);
        assertNull(responseDTO.getId());
        assertNull(responseDTO.getTitle());
        assertNull(responseDTO.getReleaseTime());
        assertEquals(DEFAULT_POSTER_PATH, responseDTO.getPosterPath());
        assertEquals(DEFAULT_RATING_SCORE, responseDTO.getRating());
    }
}
