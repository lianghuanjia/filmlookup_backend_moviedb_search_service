package com.example.movie_service.moviesearch.unit.controller;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.controller.MovieController;
import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_CODE;
import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieControllerUnitTests {

    private static final String movieId = "tt0001";

    @Mock
    private MovieService mockMovieService;

    @InjectMocks
    private MovieController movieController;

    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;


    @BeforeEach
    public void setUp() {
        title = "title";
        releasedYear = "releasedYear";
        director = "director";
        genre = "genre";
        limit = 10;
        page = 0;
        orderBy = "orderBy";
        direction = "direction";
    }

    @Test
    void searchMovieReturnOkTest() {

        // Set up: define the parameters and what should return
        MovieSearchQueryDTO movieSearchQueryDTO = new MovieSearchQueryDTO();
        List<MovieSearchQueryDTO> movieSearchQueryDTOList = new ArrayList<>();
        movieSearchQueryDTOList.add(movieSearchQueryDTO);

        CustomResponse<List<MovieSearchQueryDTO>> customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, movieSearchQueryDTOList);

        ResponseEntity<CustomResponse<List<MovieSearchQueryDTO>>> responseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);

        // What I expect my function to do, like the correct outcome I expect
        when(mockMovieService.searchMovies(any(MovieSearchParam.class)))
                .thenReturn(responseEntity);

        // Test my actual function
        ResponseEntity<CustomResponse<List<MovieSearchQueryDTO>>> actual = movieController.searchMovies(title, releasedYear, director,
                genre, limit, page, orderBy, direction);

        assertEquals(responseEntity, actual);
    }

    @Test
    void searchSingleMovieDetailsReturnOkTest() {

        // Set up
        OneMovieDetailsDTO dto = new OneMovieDetailsDTO();
        CustomResponse<OneMovieDetailsDTO> customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, dto);
        ResponseEntity<CustomResponse<OneMovieDetailsDTO>> expectedResponseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);

        // MOCK movieService behavior
        when(mockMovieService.searchOneMovieDetails(movieId)).thenReturn(expectedResponseEntity);

        // Get actual ResponseEntity
        ResponseEntity<CustomResponse<OneMovieDetailsDTO>> actualResponseEntity = movieController.searchMovieByMovieId(movieId);

        // Assert
        assertEquals(expectedResponseEntity, actualResponseEntity);
    }

}
