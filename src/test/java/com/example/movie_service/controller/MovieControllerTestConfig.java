//package com.example.movie_service.controller;
//
//import com.example.movie_service.dto.MovieSearchResultDTO;
//import com.example.movie_service.response.CustomResponse;
//import com.example.movie_service.service.MovieService;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//@TestConfiguration
//public class MovieControllerTestConfig {
//
//    @Bean
//    public MovieService movieService() {
//        return new MovieService() {
//            @Override
//            public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction) {
//                return null;
//            }
//
//            @Override
//            public List<MovieSearchResultDTO> searchMovieByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction) {
//                return null;
//            }
//        }
//    }
//}
