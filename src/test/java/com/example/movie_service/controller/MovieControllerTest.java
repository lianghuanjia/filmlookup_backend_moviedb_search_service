package com.example.movie_service.controller;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    /**
     * Why here use @MockBean instead of @Spy:
     * The annotation @Spy creates a partial mock of an actual instance. However, in a @WebMvcTest context, Spring
     * Boot automatically tries to create a mock of the service (or controller dependencies) using @MockBean,
     * not @Spy.
     */
    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testSearchMovies() throws Exception {
        List<MovieSearchResultDTO> mockMovieList = new ArrayList<>();

        mockMovieList.add(new MovieSearchResultDTO("tt1", "Movie Title", "2023",
                "Director1", "BackdropPath1", "PosterPath1", 9.0));
        CustomResponse<List<MovieSearchResultDTO>> customResponse = new CustomResponse<>(20001,
                "Movie(s) found", mockMovieList);

        Mockito.when(movieService.searchMovies(any(MovieSearchParam.class)))
                .thenReturn(new ResponseEntity<>(customResponse, HttpStatus.OK));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/api/movies")
                        .param("title", "someTitle")
                        .param("releasedYear", "2023")
                        .param("director", "Some Director")
                        .param("genre", "Action")
                        .param("limit", "10")
                        .param("page", "0")
                        .param("orderBy", "title")
                        .param("direction", "asc")
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(20001))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie(s) found"));

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("tt1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Movie Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].releaseTime").value("2023"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].directors").value("Director1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].backdropPath").value("BackdropPath1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].posterPath").value("PosterPath1"));
    }
}
