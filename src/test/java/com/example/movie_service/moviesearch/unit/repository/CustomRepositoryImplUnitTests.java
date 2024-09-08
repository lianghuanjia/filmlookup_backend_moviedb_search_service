package com.example.movie_service.moviesearch.unit.repository;


import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.CrewMember;
import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING;
import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    private String movieId;
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
    private static final String OVERVIEW = "overview";

    @BeforeEach
    public void setUp() {
        movieId = "tt1";
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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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
        List<MovieSearchQueryDTO> movieList = new ArrayList<>();
        MovieSearchQueryDTO resultDTO = new MovieSearchQueryDTO(MOVIE_ID, title, releasedYear, director,
                BACKDROP_PATH, POSTER_PATH, rating, OVERVIEW);
        movieList.add(resultDTO);

        // Use argument matchers for dynamic parameters
        when(entityManager.createNativeQuery(anyString(), anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(movieList);

        // Execute the method:
        List<MovieSearchQueryDTO> actualResult = customMovieRepositoryImpl.searchMovies(movieSearchParam);

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

    @Test
    void searchOneMovieDetails_movieFound() {
        // Set up data
        String personId = "nm1";
        String personName = "personName";
        String profilePath = "profilePath";
        String job = "job";
        OneMovieDetailsDTO oneMovieDetailsDTO = new OneMovieDetailsDTO();
        oneMovieDetailsDTO.setId(movieId);
        CrewMember crewMember = new CrewMember(personId, personName, profilePath, job);
        List<CrewMember> crewMemberList = List.of(crewMember);

        // Mock behavior the external dependencies' behaviors in other private methods and the getOneMovieBasicDetails (if any)
        // In the two private methods that getOneMovieBasicDetails uses, they use entityManager and query to get data from
        // database. Those are external dependencies, so we need to mock their behaviors.

        // Mock the behavior of getOneMovieBasicDetails
        // Why use eq(): Mockito requires consistency when using argument matchers. If you use any argument matcher
        // (like eq() or any()) in a method call, you must use argument matchers for all arguments in that method call.
        // This is why eq(SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING) is used instead of just passing
        // SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING directly.
        when(entityManager.createNativeQuery(anyString(), eq(SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING))).thenReturn(query);
        when(query.setParameter("movieId", movieId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(oneMovieDetailsDTO);

        // Mock the behavior of getCrewMembersWithProfilePic
        when(entityManager.createNativeQuery(anyString(), eq(SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING))).thenReturn(query);
        when(query.setParameter("movieId", movieId)).thenReturn(query);
        when(query.getResultList()).thenReturn(crewMemberList);

        // Act
        OneMovieDetailsDTO actual = customMovieRepositoryImpl.searchOneMovieDetails(movieId);
        assertNotNull(actual);
        assertEquals(movieId, actual.getId());

        // Assert the List<CrewMember> is not empty
        assertFalse(actual.getCrewMemberList().isEmpty());
        // Assert the first CrewMember's info
        CrewMember crewMember1 = actual.getCrewMemberList().get(0);
        assertEquals(personId, crewMember1.getPersonId());
        assertEquals(personName, crewMember1.getName());
        assertEquals(profilePath, crewMember1.getProfilePath());
        assertEquals(job, crewMember1.getJobs());
    }

    @Test
    void searchOneMovieDetails_movieNotFound() {
        // Mock behavior the external dependencies' behaviors in other private methods and the getOneMovieBasicDetails (if any)
        // In the two private methods that getOneMovieBasicDetails uses, they use entityManager and query to get data from
        // database. Those are external dependencies, so we need to mock their behaviors.

        when(entityManager.createNativeQuery(anyString(), eq(SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING))).thenReturn(query);
        when(query.setParameter("movieId", movieId)).thenReturn(query);
        doThrow(NoResultException.class).when(query).getSingleResult();

        // Act
        OneMovieDetailsDTO actual = customMovieRepositoryImpl.searchOneMovieDetails(movieId);
        assertNull(actual);

    }

    @Test
    void searchOneMovieDetails_movieFound_crewMemberNotFound() {
        // Set up data
        OneMovieDetailsDTO oneMovieDetailsDTO = new OneMovieDetailsDTO();
        oneMovieDetailsDTO.setId(movieId);

        // Mock behavior the external dependencies' behaviors in other private methods and the getOneMovieBasicDetails (if any)
        // In the two private methods that getOneMovieBasicDetails uses, they use entityManager and query to get data from
        // database. Those are external dependencies, so we need to mock their behaviors.
        when(entityManager.createNativeQuery(anyString(), eq(SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING))).thenReturn(query);
        when(query.setParameter("movieId", movieId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(oneMovieDetailsDTO);

        // Mock the behavior of getCrewMembersWithProfilePic
        when(entityManager.createNativeQuery(anyString(), eq(SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING))).thenReturn(query);
        when(query.setParameter("movieId", movieId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        OneMovieDetailsDTO actual = customMovieRepositoryImpl.searchOneMovieDetails(movieId);
        assertNotNull(actual);
        assertEquals(movieId, actual.getId());

        // Assert the List<CrewMember> is an empty list.
        assertEquals(Collections.emptyList(), actual.getCrewMemberList());
    }
}
