package com.example.movie_service.singlemoviedetails;

import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepositoryImpl;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieServiceImpl;
import com.example.movie_service.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_CODE;
import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.MOVIE_NOT_FOUND_CODE;
import static com.example.movie_service.constant.MovieConstant.MOVIE_NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpellCheckingInspection")
@ExtendWith(MockitoExtension.class)
class ServiceUnitTests {
    private static final String movieId = "tt001";
    private static final String movieTitle = "movieTitle";

    private OneMovieDetailsDTO oneMovieDetailsDTO;


    @Spy
    private ValidationService validationService;

    @Spy
    private CustomMovieRepositoryImpl customMovieRepository;

    @InjectMocks
    private MovieServiceImpl movieServiceImpl;

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
        when(customMovieRepository.searchOneMovieDetails(movieId)).thenReturn(oneMovieDetailsDTO);

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
        when(customMovieRepository.searchOneMovieDetails(movieId)).thenReturn(oneMovieDetailsDTO);

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
