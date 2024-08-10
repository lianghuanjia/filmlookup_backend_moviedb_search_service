package com.example.movie_service.service;

import com.example.movie_service.config.ResponseConstants;
import com.example.movie_service.converter.MovieSearchResultConverter;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.repository.PersonRepository;
import com.example.movie_service.response.CustomResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@Import(TestConfig.class )
public class MovieServiceTest {

    @Autowired
    private MovieServiceImpl movieServiceImpl;

    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;
    private String invalidReleasedYear;


    @BeforeEach
    public void setUp() {
        title = "title";
        releasedYear = "2020";
        director = "director";
        genre = "genre";
        limit = 10;
        page = 0;
        orderBy = "orderBy";
        direction = "direction";
        invalidReleasedYear = "2030";
    }

//    @Test
//    public void searchWithNullTitle(){
//        assertThrows(ValidationException.class, () ->
//        {movieServiceImpl.searchMovies(null, releasedYear, director, genre, limit, page, orderBy, direction);}
//        );
//
////        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = movieServiceImpl.searchMovies(
////                null, releasedYear, director, genre, limit, page, orderBy, direction);
//
////        // Make sure it returns a response entity
////        assertNotNull(responseEntity);
////
////        // Make sure it's 400 HTTP CODE
////        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
////        CustomResponse<List<MovieSearchResultDTO>> customResponse = responseEntity.getBody();
////        assertEquals(customResponse.getCode(), 40006);
////        assertEquals(customResponse.getMessage(), "Missing title");
//
//    }

    @Test
    public void searchWithInvalidYear(){
        assertThrows(ValidationException.class, () ->
                {movieServiceImpl.searchMovies(title, invalidReleasedYear, director, genre, limit, page, orderBy, direction);}
        );

//        ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> responseEntity = movieServiceImpl.searchMovies(
//                null, releasedYear, director, genre, limit, page, orderBy, direction);

//        // Make sure it returns a response entity
//        assertNotNull(responseEntity);
//
//        // Make sure it's 400 HTTP CODE
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        CustomResponse<List<MovieSearchResultDTO>> customResponse = responseEntity.getBody();
//        assertEquals(customResponse.getCode(), 40006);
//        assertEquals(customResponse.getMessage(), "Missing title");

    }

}
