package com.example.movie_service.controller;

import com.example.movie_service.config.ResponseConfig;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.response.CustomResponse;
import com.example.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class MovieController {

    private MovieService movieService;
    private ResponseConfig responseConfig;

//    // Default constructor
//    public MovieController() {
//    }

    // Constructor with dependencies injection
    @Autowired
    public MovieController(MovieService movieService, ResponseConfig responseConfig) {
        this.movieService = movieService;
        this.responseConfig = responseConfig;
    }

    /**
     * Handles GET requests for searching movies
     * @param title             the title of the movies (required, the title can also be a substring of some movie titles)
     * @param releasedYear      the year the movie was released (optional, search by substring, because in the database
     *                          the releasedYear can be YYYY or YYYY-MM-DD)
     * @param director          the director of the movie (optional, search by substring)
     * @param genre             the genre of the movie (optional)
     * @param limit             the number of results to return per page (default is 10)
     * @param page              the page number to return (default is 0)
     * @param orderBy           the field to order the results by (default is "title")
     * @param direction         the direction to order the results (default is "asc")
     * @return a ResponseEntity containing a CustomResponse with the list of movies found
     */
    @GetMapping("/movies")
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false) String releasedYear,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "title") String orderBy,
            @RequestParam(defaultValue = "asc") String direction) {

        // Validate request parameters
        if (title == null || title.isEmpty()) {
            throw new ValidationException("invalid_title");
        }

        if (releasedYear != null && Integer.parseInt(releasedYear) > Year.now().getValue()){
            System.out.println("Invalid year");
            throw new ValidationException("invalid_year");
        }

        validateParameters(limit, page, orderBy, direction);

        // Get movies from the movie service
        List<MovieSearchResultDTO> movieList = movieService.searchMovies(title, releasedYear,director,genre,limit,page,orderBy,direction);


        CustomResponse<List<MovieSearchResultDTO>> customResponse;

        // Prepare the response
        if (movieList != null){
            ResponseConfig.ResponseMessage successDetail = responseConfig.getSuccess().get("movies_found");
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), movieList);
        }
        else{
            ResponseConfig.ResponseMessage successDetail = responseConfig.getSuccess().get("movies_not_found");
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), null);
        }

        // Return the response entity
        return new ResponseEntity<>(customResponse, HttpStatus.OK);

    }

    @GetMapping("/movies/{personId}")
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovieByPersonId
            (@PathVariable String personId,
             @RequestParam(defaultValue = "10") Integer limit,
             @RequestParam(defaultValue = "1") Integer page,
             @RequestParam(defaultValue = "title") String orderBy,
             @RequestParam(defaultValue = "asc") String direction){

        // All things below should be in service layer
        // Validate the limit, page, orderBy, and direction
        validateParameters(limit, page, orderBy, direction);

        List<MovieSearchResultDTO> moviesList = movieService.searchMovieByPersonId(personId, limit, page, orderBy, direction);

        CustomResponse<List<MovieSearchResultDTO>> customResponse;

        if (moviesList != null){
            ResponseConfig.ResponseMessage successDetail = responseConfig.getSuccess().get("movies_found_with_personId");
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), moviesList);
        }
        else{
            ResponseConfig.ResponseMessage successDetail = responseConfig.getSuccess().get("movies_not_found_with_personId");
            customResponse = new CustomResponse<>(successDetail.getCode(), successDetail.getMessage(), null);
        }

        // Return the response entity
        return new ResponseEntity<>(customResponse, HttpStatus.OK);

    }





    /**
     * Validates common request parameters.
     *
     * @param limit     the number of results to return per page
     * @param page      the page number to return
     * @param orderBy   the field to order the results by
     * @param direction the direction to order the results
     */
    private void validateParameters(Integer limit, Integer page, String orderBy, String direction) {
        if (limit != null && (limit != 10 && limit != 20 && limit != 30)) {
            System.out.println("Invalid limit");
            throw new ValidationException("invalid_limit");
        }

        if (page != null && page < 1) {
            throw new ValidationException("invalid_page");
        }

        if (orderBy != null && !orderBy.equals("rating") && !orderBy.equals("title") && !orderBy.equals("releaseTime")) {
            throw new ValidationException("invalid_orderBy");
        }

        if (direction != null && !direction.equals("asc") && !direction.equals("desc")) {
            throw new ValidationException("invalid_direction");
        }
    }
}
