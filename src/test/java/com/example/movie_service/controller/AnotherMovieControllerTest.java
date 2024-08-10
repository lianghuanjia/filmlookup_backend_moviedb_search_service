package com.example.movie_service.controller;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.MOVIE_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnotherMovieControllerTest {

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
        String title = "title";
        String releasedYear = "releasedYear";
        String director = "director";
        String genre = "genre";
        Integer limit = 10;
        Integer page = 0;
        String orderBy = "orderBy";
        String direction = "direction";
    }

    @Test
    public void searchMovieReturnOkTest(){
        MovieSearchResultDTO movieSearchResultDTO = new MovieSearchResultDTO();
        List<MovieSearchResultDTO> movieSearchResultDTOList = new ArrayList<>();
        movieSearchResultDTOList.add(movieSearchResultDTO);

        CustomResponse<List<MovieSearchResultDTO>> customResponse = new CustomResponse<>(20001, MOVIE_FOUND, movieSearchResultDTOList);

        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);

        when(mockMovieService.searchMovies(any(),any(),any(),any(),any(),any(),any(),any()))
                .thenReturn(responseEntity);

        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> actual = movieController.searchMovies(title, releasedYear, director,
        genre, limit, page, orderBy, direction);

        assertEquals(responseEntity, actual);

    }

}
