package com.example.movie_service.controller;


//import com.example.movie_service.config.ErrorResponseConfig;
//import com.example.movie_service.config.SuccessResponseConfig;
import com.example.movie_service.config.ResponseConfig;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class MovieController {

    private MovieService movieService;
//    private SuccessResponseConfig successResponseConfig;
    private ResponseConfig responseConfig;

    public MovieController() {
    }

    @Autowired
    public MovieController(MovieService movieService, ResponseConfig responseConfig) {
        this.movieService = movieService;
        this.responseConfig = responseConfig;
    }

    @GetMapping("/movies")
    public ResponseEntity<CustomResponse<List<Movie>>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false) String releasedYear,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "title") String orderBy,
            @RequestParam(defaultValue = "asc") String direction) {

        // Validate request parameters
        if (title == null || title.isEmpty()) {
            throw new ValidationException("invalid_title");
        }

        if (releasedYear != null && Integer.valueOf(releasedYear) > Year.now().getValue()){
            System.out.println("Invalid year");
            throw new ValidationException("invalid_year");
        }

        if (limit != null && (limit != 10 && limit != 20 && limit != 30)){
            System.out.println("Invalid limit");
            throw new ValidationException("invalid_limit");
        }

        if (page != null && page < 0){
            throw new ValidationException("invalid_page");
        }

        if (orderBy != null && !orderBy.equals("rating") && !orderBy.equals("title") && !orderBy.equals("year")){
            throw new ValidationException("invalid_orderBy");
        }

        if (direction != null && !direction.equals("asc") && !direction.equals("desc")){
            throw new ValidationException("invalid_direction");
        }

        // Get movies from the movie service
        List<Movie> movieList = movieService.searchMovies();

        ResponseConfig.ResponseMessage successDetail = responseConfig.getSuccess().get("movies_found");
        CustomResponse<List<Movie>> customResponse;

        if (movieList != null){
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), movieList);
        }
        else{
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), null);
        }
        return new ResponseEntity<>(customResponse, HttpStatus.OK);

    }




}
