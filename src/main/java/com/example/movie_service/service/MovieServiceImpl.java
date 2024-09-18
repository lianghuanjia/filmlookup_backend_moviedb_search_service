package com.example.movie_service.service;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.converter.MovieSearchQueryToResponseConverter;
import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import com.example.movie_service.dto.MovieSearchResultWithPaginationDTO;
import com.example.movie_service.dto.MovieSearchWithTitleDTOFromRepoToService;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.response.CustomResponse;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.movie_service.constant.MovieConstant.*;

/**
 * Implementation of MovieService interface
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final CustomMovieRepository movieRepository;
    private final ValidationService validationService;
    private final MovieSearchQueryToResponseConverter converter;


    /**
     * Constructor with dependencies injection.
     *
     * @param movieRepository the repository for accessing movie data
     */
    @Autowired
    public MovieServiceImpl(CustomMovieRepository movieRepository, ValidationService validationService,
                            MovieSearchQueryToResponseConverter converter) {
        this.movieRepository = movieRepository;
        this.validationService = validationService;
        this.converter = converter;
    }

    /**
     * Searches for movies based on various criteria.
     *
     * @return a list of movies that match the search criteria
     */
    @Override
    public ResponseEntity<CustomResponse<MovieSearchResultWithPaginationDTO>> searchMovies(MovieSearchParam movieSearchParam)
            throws PersistenceException, ValidationException {

        String title = movieSearchParam.getTitle();
        String releasedYear = movieSearchParam.getReleasedYear();
        Integer limit = movieSearchParam.getLimit();
        Integer pageNumberFromFrontend = movieSearchParam.getPage();

        // Convert the page in movieSearchParam to zero index because its OFFSET, and in SQL OFFSET is 0-indexed
        movieSearchParam.setPage(movieSearchParam.getPage()-1);

        Integer zeroIndexPage = movieSearchParam.getPage();
        String orderBy = movieSearchParam.getOrderBy();
        String direction = movieSearchParam.getDirection();

        // Validate parameters:
        validateSearchMoviesParameters(title, releasedYear, limit, zeroIndexPage, orderBy, direction);



        // Get search results from repository layer
        // If the query times out, it's possible there will be QueryTimeoutException or PersistenceException
        MovieSearchWithTitleDTOFromRepoToService queryDTO = movieRepository.searchMovies(movieSearchParam);

        List<MovieSearchQueryDTO> queryResults = queryDTO.getMovies();

        int totalItems = queryDTO.getTotalItem();

        // Prepare the response's code and message
        CustomResponse<MovieSearchResultWithPaginationDTO> customResponse;

        // Perform the DTO conversions
        List<MovieSearchResponseDTO> responseList = queryResults.stream()
                .map(converter::convert)
                .filter(Objects::nonNull)  // Removes null elements from the stream
                .toList();

        int itemsPerPage = movieSearchParam.getLimit();
        int currentPage = pageNumberFromFrontend; // Page index starts from 0, not 1
        int totalPages = (int) Math.ceil((double)totalItems / itemsPerPage);

        MovieSearchResultWithPaginationDTO resultWithPaginationDTO = new MovieSearchResultWithPaginationDTO();
        resultWithPaginationDTO.setMovies(responseList);
        resultWithPaginationDTO.setTotalItems(totalItems);
        resultWithPaginationDTO.setCurrentPage(currentPage);
        resultWithPaginationDTO.setItemsPerPage(itemsPerPage);
        resultWithPaginationDTO.setTotalPages(totalPages);
        resultWithPaginationDTO.setHasNextPage(currentPage < totalPages-1);
        resultWithPaginationDTO.setHasPrevPage(currentPage > 0);

        // If no movie is found, return a custom response with movie not found code and message inside, with the empty
        // movieList. Before I put null in the data. This is not good because it might cause NullPointerExceptions. It
        // also simplifies the handling of responses, as clients don't need to check for both 'null' and empty conditions.
        if (responseList.isEmpty()) {
            customResponse = new CustomResponse<>(MOVIE_NOT_FOUND_CODE, MOVIE_NOT_FOUND_MESSAGE, null);
        } else {
            customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, resultWithPaginationDTO);
        }


        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<CustomResponse<OneMovieDetailsDTO>> searchOneMovieDetails(String movieId) {
        // validate movieId
        validationService.validateMovieId(movieId);

        // Get result from repository layer
        OneMovieDetailsDTO oneMovieDetailsDTO = movieRepository.searchOneMovieDetails(movieId);

        // Generate custom response:
        CustomResponse<OneMovieDetailsDTO> customResponse;
        ResponseEntity<CustomResponse<OneMovieDetailsDTO>> responseEntity;
        if (oneMovieDetailsDTO != null) {
            customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, oneMovieDetailsDTO);
        } else {
            customResponse = new CustomResponse<>(MOVIE_NOT_FOUND_CODE, MOVIE_NOT_FOUND_MESSAGE, null);
        }
        responseEntity = new ResponseEntity<>(customResponse, HttpStatus.OK);
        return responseEntity;
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
