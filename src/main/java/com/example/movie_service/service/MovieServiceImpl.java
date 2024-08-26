package com.example.movie_service.service;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.response.CustomResponse;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.movie_service.constant.MovieConstant.*;

/**
 * Implementation of MovieService interface
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final CustomMovieRepository movieRepository;
    private final ValidationService validationService;


    /**
     * Constructor with dependencies injection.
     *
     * @param movieRepository the repository for accessing movie data
     */
    @Autowired
    public MovieServiceImpl(CustomMovieRepository movieRepository,
                            ValidationService validationService) {
        this.movieRepository = movieRepository;
        this.validationService = validationService;
    }

    /**
     * Searches for movies based on various criteria.
     *
     * @return a list of movies that match the search criteria
     */
    @Override
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(MovieSearchParam movieSearchParam)
            throws PersistenceException, ValidationException {

        String title = movieSearchParam.getTitle();
        String releasedYear = movieSearchParam.getReleasedYear();
        Integer limit = movieSearchParam.getLimit();
        Integer page = movieSearchParam.getPage();
        String orderBy = movieSearchParam.getOrderBy();
        String direction = movieSearchParam.getDirection();

        // Validate parameters:
        validateSearchMoviesParameters(title, releasedYear, limit, page, orderBy, direction);

        // Get search results from repository layer
        // If the query times out, it's possible there will be QueryTimeoutException or PersistenceException
        List<MovieSearchResultDTO> movieList = movieRepository.searchMovies(movieSearchParam);

        // Prepare the response's code and message
        CustomResponse<List<MovieSearchResultDTO>> customResponse;

        // If no movie is found, return a custom response with movie not found code and message inside, with the empty
        // movieList. Before I put null in the data. This is not good because it might cause NullPointerExceptions. It
        // also simplifies the handling of responses, as clients don't need to check for both 'null' and empty conditions.
        if (movieList.isEmpty()) {
            customResponse = new CustomResponse<>(MOVIE_NOT_FOUND_CODE, MOVIE_NOT_FOUND_MESSAGE, movieList);
        } else {
            customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, movieList);
        }


        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    /**
     * Validate those parameters and make sure they are valid
     * @param title the searching movie's title
     * @param releasedYear the released year of the searching movie
     * @param limit the maximum number of the return results
     * @param page the page to do the pagination
     * @param orderBy the field that orders the return results. It can be title/rating/releasedYear
     * @param direction the direction that orders the results.
     */
    private void validateSearchMoviesParameters(String title, String releasedYear, Integer limit, Integer page,
                                                String orderBy, String direction) {
        validationService.validateTitle(title);
        validationService.validateReleasedYear(releasedYear);
        validationService.validateLimit(limit);
        validationService.validatePage(page);
        validationService.validateOrderBy(orderBy);
        validationService.validateDirection(direction);
    }

}
