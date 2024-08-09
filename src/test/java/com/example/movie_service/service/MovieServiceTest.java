package com.example.movie_service.service;

import com.example.movie_service.controller.MovieController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(MovieController.class)
@Import(TestConfig.class)
public class MovieServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;


    @Test
    public void testGetMovieInvalidYear() throws Exception {
        int futureYear = LocalDate.now().getYear()+1;

        mockMvc.perform(get("/v1/api/movies")
                        .param("title","The Dark Knight")
                        .param("releasedYear", String.valueOf(futureYear)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40001)))
                .andExpect(jsonPath("$.message", is("Invalid year")));
    }

    @Test
    public void testGetMovieInvalidLimit() throws Exception{
        Integer invalidLimit = 11;

        mockMvc.perform(get("/v1/api/movies")
                        .param("title", "The Dark Knight")
                        .param("limit", invalidLimit.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40002)))
                .andExpect(jsonPath("$.message", is("Invalid limit")));
    }

    @Test
    public void testGetMovieInvalidPage() throws Exception{
        Integer invalidPage = -1;

        mockMvc.perform(get("/v1/api/movies")
                        .param("title", "The Dark Knight")
                        .param("page", invalidPage.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40003)))
                .andExpect(jsonPath("$.message", is("Invalid page")));
    }

    @Test
    public void testGetMovieInvalidOrderBy() throws Exception{
        Integer invalidOrderBy = 4;

        mockMvc.perform(get("/v1/api/movies")
                        .param("title", "The Dark Knight")
                        .param("orderBy", invalidOrderBy.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40004)))
                .andExpect(jsonPath("$.message", is("Invalid orderBy")));
    }

    @Test
    public void testGetMovieWithRandomOrderBy() throws Exception{
        String randomOrderBy = "@#qpfhwqpoih3p";

        mockMvc.perform(get("/v1/api/movies")
                        .param("title", "The Dark Knight")
                        .param("orderBy", randomOrderBy))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40004)))
                .andExpect(jsonPath("$.message", is("Invalid orderBy")));
    }


    @Test
    public void testGetMovieWithRandomDirectionInput() throws Exception{
        String randomDirection = "@#qpfhwqpoih3p";

        mockMvc.perform(get("/v1/api/movies")
                        .param("title", "The Dark Knight")
                        .param("direction", randomDirection))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40005)))
                .andExpect(jsonPath("$.message", is("Invalid direction")));
    }

    @Test
    public void testGetMovieWithMissingTitle() throws Exception{
mockMvc.perform(get("/v1/api/movies"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(40006)))
                .andExpect(jsonPath("$.message", is("Missing title")));
    }


}
