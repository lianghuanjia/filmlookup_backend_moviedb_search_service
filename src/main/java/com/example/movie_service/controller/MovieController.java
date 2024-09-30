package com.example.movie_service.controller;

import com.example.movie_service.builder.MovieSearchParam;
import lombok.extern.slf4j.Slf4j;
import com.example.movie_service.dto.MovieSearchResultWithPaginationDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller layer of the movie_service application
 */
@Validated
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v1/api")
public class MovieController {

    private final MovieService movieService;

    // Constructor with dependencies injection
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check received");
        return ResponseEntity.ok("Healthy");
    }

    /**
     * Handles GET requests for searching movies
     *
     * @param title        the title of the movies (required, the title can also be a substring of some movie titles)
     * @param releasedYear the year the movie was released (optional, search by substring, because in the database
     *                     the releasedYear can be YYYY or YYYY-MM-DD)
     * @param director     the director of the movie (optional, search by substring)
     * @param genre        the genre of the movie (optional)
     * @param limit        the number of results to return per page (default is 10)
     * @param page         the page number to return (default is 0)
     * @param orderBy      the field to order the results by (default is "title", can also be "releaseTime", "rating")
     * @param direction    the direction to order the results (default is "asc", can also be "desc")
     * @return a ResponseEntity containing a CustomResponse with the list of movies found
     */
    @GetMapping("/movies")
    public ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false) String releasedYear,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "title") String orderBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        log.info("Searching movies with title: {}, releasedYear: {}, director: {}, genre: {}, limit: {}, page: {}, orderBy: {}, direction: {}",
                title, releasedYear, director, genre, limit, page, orderBy, direction);

        // Build the MovieSearchRequestParam using the builder pattern
        MovieSearchParam movieSearchRequestParam = MovieSearchParam.builder()
                .title(title)
                .releasedYear(releasedYear)
                .director(director)
                .genre(genre)
                .limit(limit)
                .page(page)
                .orderBy(orderBy)
                .direction(direction)
                .build();

        // Return the response entity
        return movieService.searchMovies(movieSearchRequestParam);

    }

    @GetMapping("movies/{movie_id}")
    public ResponseEntity<CustomResponse<OneMovieDetailsDTO>> searchMovieByMovieId(@PathVariable("movie_id") String movieId){

        log.info("Searching movie with ID: {}", movieId);

        return movieService.searchOneMovieDetails(movieId);
    }

}
