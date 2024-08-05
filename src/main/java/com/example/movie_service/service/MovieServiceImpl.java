package com.example.movie_service.service;

import com.example.movie_service.config.ResponseConfig;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.exception.ResourceNotFoundException;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.repository.MovieRepository;
import com.example.movie_service.repository.PersonRepository;
import com.example.movie_service.response.CustomResponse;
import org.apache.el.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

/**
 * Implementation of MovieService interface
 */
@Service
public class MovieServiceImpl implements MovieService {

    private CustomMovieRepository movieRepository; // Should I make this final?
    private PersonRepository personRepository;
    private ValidationService validationService;
    private ResponseConfig responseConfig;
    // Default constructor
    public MovieServiceImpl() {
    }

    /**
     * Constructor with dependencies injection.
     * @param movieRepository the repository for accessing movie data
     */
    @Autowired
    public MovieServiceImpl(CustomMovieRepository movieRepository, PersonRepository personRepository,
                            ValidationService validationService, ResponseConfig responseConfig) {
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.validationService = validationService;
        this.responseConfig = responseConfig;
    }

    /**
     * Searches for movies based on various criteria.
     * @return a list of movies that match the search criteria
     */
    @Override
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(String title, String releasedYear, String director, String genre,
                                                   Integer limit, Integer page, String orderBy, String direction) {

        // Validate parameters:
        validationService.validateTitle(title);
        validationService.validateReleasedYear(releasedYear);
        validationService.validateLimit(limit);
        validationService.validatePage(page);
        validationService.validateOrderBy(orderBy);
        validationService.validateDirection(direction);

        List<MovieSearchResultDTO> movieList = movieRepository.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

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
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    /**
     * Searches for a movie by its ID.
     * @param personId the ID of the movie to search for
     * @return the movie that matches the given ID
     */
    @Override
    public List<MovieSearchResultDTO> searchMovieByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction) {
        // Validate if personId exists in database
        if (!personRepository.existsById(personId)){
            throw new ResourceNotFoundException("personId");
        }

        List<MovieSearchResultDTO> movieSearchResultDTOList = movieRepository.searchMoviesByPersonId(personId, limit, page, orderBy, direction);


        return movieSearchResultDTOList;
    }
}
