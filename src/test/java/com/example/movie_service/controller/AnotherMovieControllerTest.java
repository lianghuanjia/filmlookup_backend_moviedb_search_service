package com.example.movie_service.controller;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnotherMovieControllerTest {

    @Spy
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
    void searchMovieReturnOkTest(){

        // Set up: define the parameters and what should return
        MovieSearchResultDTO movieSearchResultDTO = new MovieSearchResultDTO();
        List<MovieSearchResultDTO> movieSearchResultDTOList = new ArrayList<>();
        movieSearchResultDTOList.add(movieSearchResultDTO);

        CustomResponse<List<MovieSearchResultDTO>> customResponse = new CustomResponse<>(20001, MOVIE_FOUND_MESSAGE, movieSearchResultDTOList);

        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);

        // What I expect my function to do, like the correct outcome I expect
        when(mockMovieService.searchMovies(any(),any(),any(),any(),any(),any(),any(),any()))
                .thenReturn(responseEntity);

        // Test my actual function
        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> actual = movieController.searchMovies(title, releasedYear, director,
        genre, limit, page, orderBy, direction);

        assertEquals(responseEntity, actual);
    }

}
