package com.example.movie_service.repository;


import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
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
class CustomRepositoryImplTest {

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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        movieSearchParam = movieSearchParam.toBuilder().releasedYear(null).build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        movieSearchParam = movieSearchParam.toBuilder().releasedYear("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        movieSearchParam = movieSearchParam.toBuilder().director("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        movieSearchParam = movieSearchParam.toBuilder().genre("").build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
        String id = "tt1";
        String backdropPath = "backdropPath";
        String posterPath = "posterPath";

        movieSearchParam = movieSearchParam.toBuilder().releasedYear(null).director(null).genre(null)
                .build();

        // Mock the behavior
        List<MovieSearchResultDTO> movieList = new ArrayList<>();
        MovieSearchResultDTO resultDTO = new MovieSearchResultDTO(id, title, releasedYear, director,
                backdropPath, posterPath, rating);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchResultDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

        // Verify if the actualResult is the same as we define
        assertEquals(actualResult.get(0).getId(), id);
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
