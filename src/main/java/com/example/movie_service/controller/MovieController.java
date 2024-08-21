package com.example.movie_service.controller;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/api")
public class MovieController {

    private final MovieService movieService;

    // Constructor with dependencies injection
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
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
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false) String releasedYear,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "title") String orderBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        // Build the MovieSearchRequestParam using the builder pattern
        MovieSearchParam movieSearchRequestParam = MovieSearchParam.builder()
                .title(title)
                .releasedYear(releasedYear)
                .direction(director)
                .genre(genre)
                .limit(limit)
                .page(page)
                .orderBy(orderBy)
                .direction(direction)
                .build();

        // Return the response entity
        return movieService.searchMovies(movieSearchRequestParam);

    }

}
