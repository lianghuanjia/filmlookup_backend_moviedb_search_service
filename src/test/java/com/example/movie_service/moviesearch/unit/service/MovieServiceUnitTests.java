package com.example.movie_service.moviesearch.unit.service;


import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.converter.MovieSearchQueryToResponseConverter;
import com.example.movie_service.dto.MovieSearchResultWithPaginationDTO;
import com.example.movie_service.dto.MovieSearchWithTitleDTOFromRepoToService;
import com.example.movie_service.dto.MovieTitleSearchSQLQueryResultDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieServiceImpl;
import com.example.movie_service.service.ValidationService;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceUnitTests {

    private static final String movieId = "tt001";
    private static final String movieTitle = "movieTitle";

    private OneMovieDetailsDTO oneMovieDetailsDTO;


    @Mock
    private CustomMovieRepository movieRepository; // Should I make this final?

    @Mock
    private ValidationService validationService;

    @Mock
    private MovieSearchQueryToResponseConverter converter;

    @InjectMocks
    private MovieServiceImpl movieServiceImpl; // Should this be MovieService or MovieServiceImpl?

    private MovieSearchParam movieSearchParam;

    @BeforeEach
    public void setUp() {

        // Build the basic valid MovieSearchParam
        // Note: here because the search param is from the frontend, so the page starts at 1 instead of 0
        movieSearchParam = MovieSearchParam.builder()
                .title("title").releasedYear("2020").director("director").genre("genre").limit(10).page(1)
                .orderBy("orderBy").direction("orderBy").build();

    }

    @Test
    void searchWithNullTitle() {
        movieSearchParam = movieSearchParam.toBuilder().title(null).build();

        //since underneath, it uses validationService, and this service is not what we are testing right now, we need to mock its behavior
        doThrow(ValidationException.class).when(validationService).validateTitle(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies(movieSearchParam));
    }

    @Test
    void searchWithInvalidYear() {
        movieSearchParam = movieSearchParam.toBuilder().releasedYear("2039").build();

        doNothing().when(validationService).validateTitle(any());
        doThrow(ValidationException.class).when(validationService).validateReleasedYear(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidLimit() {
        Integer invalidLimit = 15;

        movieSearchParam = movieSearchParam.toBuilder().limit(invalidLimit).build();

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doThrow(ValidationException.class).when(validationService).validateLimit(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidPage() {
        Integer invalidPage = -1;

        movieSearchParam = movieSearchParam.toBuilder().page(invalidPage).build();

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doThrow(ValidationException.class).when(validationService).validatePage(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidOrderBy() {
        String invalidOrderBy = "time";

        movieSearchParam = movieSearchParam.toBuilder().orderBy(invalidOrderBy).build();

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doThrow(ValidationException.class).when(validationService).validateOrderBy(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a ValidationException");
    }

    @Test
    void searchWithInvalidDirection() {
        String invalidDirection = "north";

        movieSearchParam = movieSearchParam.toBuilder().direction(invalidDirection).build();

        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doThrow(ValidationException.class).when(validationService).validateDirection(any());

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a ValidationException");
    }


    @Test
    void searchMovieTimeout() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(QueryTimeoutException.class).when(movieRepository).searchMovies(movieSearchParam);

        assertThrows(QueryTimeoutException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a QueryTimeoutException");
    }

    @Test
    void searchMovieThrowPersistenceException() {
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());
        doThrow(PersistenceException.class).when(movieRepository).searchMovies(movieSearchParam);

        assertThrows(PersistenceException.class, () -> movieServiceImpl.searchMovies
                        (movieSearchParam),
                "searchMovies did not throw a PersistenceException");
    }

    @Test
    void searchMovieReturnListOfMovieSearchResultDTO() {
        // Setup mock data
        List<MovieTitleSearchSQLQueryResultDTO> mockMovies = List.of(
                new MovieTitleSearchSQLQueryResultDTO("1", "Inception", "2010", "Christopher Nolan", "path/to/backdrop", "path/to/poster", 9.0, "overview")
        );

        MovieSearchWithTitleDTOFromRepoToService dtoFromRepoToService = new MovieSearchWithTitleDTOFromRepoToService(1, mockMovies);

        // Mock the repository call
        when(movieRepository.searchMovies(movieSearchParam))
                .thenReturn(dtoFromRepoToService);

        // Since we use @Mock on the converter, All methods on this mock return default values (e.g., null, 0, empty collections)
        // unless explicitly stubbed (i.e., told to return specific values). Therefore, we stub the behavior below for the converter
        when(converter.convert(any(MovieTitleSearchSQLQueryResultDTO.class)))
                .thenReturn(new MovieSearchResponseDTO("1", "Inception", "2010", "path/to/poster", 9.0, "overview"));


        // Ensure validation service calls do nothing (if necessary)
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());

        // Call the actual method under test
        ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> actualResponseEntity = movieServiceImpl.searchMovies(
                movieSearchParam);

        // Verify the results
        assertNotNull(actualResponseEntity.getBody());
        assertNotNull(actualResponseEntity.getBody().getData());
        MovieSearchResultWithPaginationDTO dto = actualResponseEntity.getBody().getData();
        assertEquals(1, dto.getTotalItems());
        assertEquals(1, dto.getMovies().size());
        assertEquals(1, dto.getCurrentPage());
        assertEquals(1, dto.getTotalPages());
        assertFalse(dto.isHasNextPage());
        assertFalse(dto.isHasPrevPage());
        System.out.println(actualResponseEntity.getBody().getData());
        assertEquals("Inception", dto.getMovies().get(0).getTitle());
    }


    @Test
    void searchMovieWithNoResult() {
        List<MovieTitleSearchSQLQueryResultDTO> mockMovies = List.of();
        MovieSearchWithTitleDTOFromRepoToService dtoFromRepoToService = new MovieSearchWithTitleDTOFromRepoToService(0, mockMovies);

        // Mock the repository call
        when(movieRepository.searchMovies(movieSearchParam))
                .thenReturn(dtoFromRepoToService);

        // Ensure validation service calls do nothing (if necessary)
        doNothing().when(validationService).validateTitle(any());
        doNothing().when(validationService).validateReleasedYear(any());
        doNothing().when(validationService).validateLimit(any());
        doNothing().when(validationService).validatePage(any());
        doNothing().when(validationService).validateOrderBy(any());
        doNothing().when(validationService).validateDirection(any());

        ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> actualResponseEntity = movieServiceImpl.searchMovies(
                movieSearchParam
        );

        assertNotNull(actualResponseEntity.getBody());
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(MOVIE_NOT_FOUND_CODE, actualResponseEntity.getBody().getCode());
        assertEquals(MOVIE_NOT_FOUND_MESSAGE, actualResponseEntity.getBody().getMessage());
        assertNull(actualResponseEntity.getBody().getData());
    }


    @Test
    void searchOneMovieDetailsFoundMovie(){
        // Set up
        oneMovieDetailsDTO = new OneMovieDetailsDTO();
        oneMovieDetailsDTO.setId(movieId);
        oneMovieDetailsDTO.setTitle(movieTitle);

        // mock methods behaviors
        // Assume the movieId is valid and validationService doesn't throw Exception
        doNothing().when(validationService).validateMovieId(anyString());

        // Assume the movieRepository found a movie based on the movieId and returns a oneMovieDetailsDTO
        when(movieRepository.searchOneMovieDetails(movieId)).thenReturn(oneMovieDetailsDTO);

        ResponseEntity<CustomResponse<OneMovieDetailsDTO>>actualResponseEntity = movieServiceImpl.searchOneMovieDetails(movieId);

        // Assert
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertNotNull(actualResponseEntity.getBody());
        CustomResponse<OneMovieDetailsDTO> actualCustomResponse = actualResponseEntity.getBody();
        assertEquals(MOVIE_FOUND_CODE, actualCustomResponse.getCode());
        assertEquals(MOVIE_FOUND_MESSAGE, actualCustomResponse.getMessage());
        // Assert the OneMovieDetailsDTO is not null
        assertNotNull(actualCustomResponse.getData());
        OneMovieDetailsDTO actualDTO = actualCustomResponse.getData();
        // Assert the OneMovieDetailsDTO's id matches the movieId and its title matches the movieTitle
        assertEquals(movieId, actualDTO.getId());
        assertEquals(movieTitle, actualDTO.getTitle());
    }

    @Test
    void searchOneMovieDetailsDoesNotFoundMovie(){
        // Set up
        oneMovieDetailsDTO = null;

        // mock methods behaviors
        // Assume the movieId is valid and validationService doesn't throw Exception
        doNothing().when(validationService).validateMovieId(anyString());
        // Assume the movieRepository found a movie based on the movieId and returns a oneMovieDetailsDTO
        when(movieRepository.searchOneMovieDetails(movieId)).thenReturn(oneMovieDetailsDTO);

        ResponseEntity<CustomResponse<OneMovieDetailsDTO>>actualResponseEntity = movieServiceImpl.searchOneMovieDetails(movieId);

        // Assert
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertNotNull(actualResponseEntity.getBody());
        CustomResponse<OneMovieDetailsDTO> actualCustomResponse = actualResponseEntity.getBody();
        assertEquals(MOVIE_NOT_FOUND_CODE, actualCustomResponse.getCode());
        assertEquals(MOVIE_NOT_FOUND_MESSAGE, actualCustomResponse.getMessage());
        // Assert the OneMovieDetailsDTO is null
        assertNull(actualCustomResponse.getData());
    }

    @Test
    void searchOneMovieDetailsInvalidMovieId_MovieIdBeingNull(){
        // Mock the validationService.validateMovieId(movieId) line throws an Exception.
        // We assume the validationService.validateMovieId(movieId) work as we expected. We need to test this
        // method too.
        doThrow(ValidationException.class).when(validationService).validateMovieId(null);

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchOneMovieDetails(null));
    }

    @Test
    void searchOneMovieDetailsInvalidMovieId_MovieIdBeingEmptyString(){
        doThrow(ValidationException.class).when(validationService).validateMovieId("");

        assertThrows(ValidationException.class, () -> movieServiceImpl.searchOneMovieDetails(""));

    }

}
