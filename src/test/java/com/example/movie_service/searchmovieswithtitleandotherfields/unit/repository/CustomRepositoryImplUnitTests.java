package com.example.movie_service.searchmovieswithtitleandotherfields.unit.repository;


import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomRepositoryImplUnitTests {

    @Mock
    private EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    private CustomMovieRepositoryImpl customMovieRepositoryImpl;

    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private Double rating;

    private MovieSearchParam movieSearchParam;

    private static final String MOVIE_ID = "tt1";
    private static final String BACKDROP_PATH = "backdropPath";
    private static final String POSTER_PATH = "posterPath";

    @BeforeEach
    public void setUp() {
        title = "title";
        releasedYear = "2020";
        director = "director";
        genre = "genre";
        limit = 10;
        page = 0;
        String orderBy = "orderBy";
        String direction = "direction";
        rating = 9.0;

        movieSearchParam = MovieSearchParam.builder().title(title).releasedYear(releasedYear).director(director)
                .genre(genre).limit(limit).page(page).orderBy(orderBy).direction(direction).build();
    }

    @Test
    void repositoryImplSearchMovies() {

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query).setParameter("releasedYear", releasedYear + "%");
        verify(query).setParameter("director", "%" + director + "%");
        verify(query).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }

    @Test
    void repositoryImplSearchMoviesWithoutReleasedYear() {

        movieSearchParam = movieSearchParam.toBuilder().releasedYear(null).build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query, never()).setParameter("releasedYear", releasedYear + "%");
        verify(query).setParameter("director", "%" + director + "%");
        verify(query).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }

    @Test
    void repositoryImplSearchMoviesWithReleasedYearBeingEmpty() {

        movieSearchParam = movieSearchParam.toBuilder().releasedYear("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query, never()).setParameter("releasedYear", releasedYear + "%");
        verify(query).setParameter("director", "%" + director + "%");
        verify(query).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }

    @Test
    void repositoryImplSearchMoviesWithDirectorBeingEmpty() {

        movieSearchParam = movieSearchParam.toBuilder().director("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query).setParameter("releasedYear", releasedYear + "%");
        verify(query, never()).setParameter("director", "%" + director + "%");
        verify(query).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }


    @Test
    void repositoryImplSearchMoviesWithGenreBeingEmpty() {

        movieSearchParam = movieSearchParam.toBuilder().genre("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query).setParameter("releasedYear", releasedYear + "%");
        verify(query).setParameter("director", "%" + director + "%");
        verify(query, never()).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }

    @Test
    void repositoryImplSearchMoviesWithTitleOnly() {

        movieSearchParam = movieSearchParam.toBuilder().releasedYear(null).director(null).genre(null)
                .build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(MOVIE_ID, actualResult.get(0).getId());
        assertEquals(1, actualResult.size());

        // Verify all setParameters methods are called given the query parameters we provide
        verify(query).setParameter("title", "%" + title + "%");
        verify(query, never()).setParameter("releasedYear", releasedYear + "%");
        verify(query, never()).setParameter("director", "%" + director + "%");
        verify(query, never()).setParameter("genre", "%" + genre + "%");
        verify(query).setParameter("limit", limit);
        verify(query).setParameter("offset", page * limit);
    }

}
