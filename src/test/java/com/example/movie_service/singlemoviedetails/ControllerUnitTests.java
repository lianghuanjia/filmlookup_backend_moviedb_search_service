package com.example.movie_service.singlemoviedetails;

import com.example.movie_service.controller.MovieController;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_CODE;
import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerUnitTests {

    private static final String movieId = "tt0001";

    @Spy
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @Test
    void searchSingleMovieDetailsReturnOkTest() {

        // Set up
        OneMovieDetailsDTO dto = new OneMovieDetailsDTO();
        CustomResponse<OneMovieDetailsDTO> customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, dto);
        ResponseEntity<CustomResponse<OneMovieDetailsDTO>> expectedResponseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);

        // MOCK movieService behavior
        when(movieService.searchOneMovieDetails(movieId)).thenReturn(expectedResponseEntity);

        // Get actual ResponseEntity
        ResponseEntity<CustomResponse<OneMovieDetailsDTO>> actualResponseEntity = movieController.searchMovieByMovieId(movieId);

        // Assert
        assertEquals(expectedResponseEntity, actualResponseEntity);
    }
}
