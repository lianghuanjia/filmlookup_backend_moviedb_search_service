package com.example.movie_service.service;

import com.example.movie_service.converter.MovieSearchResultConverter;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.exception.ResourceNotFoundException;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.repository.CustomMovieRepository;
import com.example.movie_service.repository.PersonRepository;
import com.example.movie_service.response.CustomResponse;
import jakarta.persistence.PersistenceException;
import org.hibernate.QueryTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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

    private final CustomMovieRepository movieRepository; // Should I make this final?
    private final PersonRepository personRepository;
    private final ValidationService validationService;
    private final MovieSearchResultConverter movieSearchResultConverter;
    private final ConversionService conversionService;

    /**
     * Constructor with dependencies injection.
     * @param movieRepository the repository for accessing movie data
     */
    @Autowired
    public MovieServiceImpl (CustomMovieRepository movieRepository, PersonRepository personRepository,
                            ValidationService validationService, MovieSearchResultConverter movieSearchResultConverter,
                             ConversionService conversionService) {
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.validationService = validationService;
        this.movieSearchResultConverter = movieSearchResultConverter;
        this.conversionService = conversionService;
    }

    /**
     * Searches for movies based on various criteria.
     * @return a list of movies that match the search criteria
     */
    @Override
    public ResponseEntity<CustomResponse<List<MovieSearchResultDTO>>> searchMovies(String title, String releasedYear,
                                                                                    String director, String genre,
                                                                                    Integer limit, Integer page,
                                                                                    String orderBy, String direction)
            throws QueryTimeoutException, PersistenceException, ValidationException
            {
        // Validate parameters:
        validateSearchMoviesParameters(title, releasedYear, limit, page, orderBy, direction);

        // Get search results from repository layer
        // If the query times out, it's possible there will be QueryTimeoutException or PersistenceException
        List<MovieSearchResultDTO> movieList = movieRepository.searchMovies(title, releasedYear, director, genre, limit, page, orderBy, direction);

//        // Convert the search results to a List of DTO
//        List<MovieSearchResultDTO> mappedResults = movieSearchResultConverter.convertList(movieList);
//        System.out.println("Movies found: " + movieList.size());
        // Prepare the response's code and message
        CustomResponse<List<MovieSearchResultDTO>> customResponse;

        // If no movie is found, return a custom response with movie not found code and message inside, with the empty
        // movieList. Before I put null in the data. This is not good because it might cause NullPointerExceptions. It
        // also simplifies the handling of responses, as clients don't need to check for both 'null' and empty conditions.
        if (movieList.isEmpty()){
            customResponse = new CustomResponse<>(MOVIE_NOT_FOUND_CODE, MOVIE_NOT_FOUND_MESSAGE, movieList);
        } else{
            customResponse = new CustomResponse<>(MOVIE_FOUND_CODE, MOVIE_FOUND_MESSAGE, movieList);
        }


        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    private void validateSearchMoviesParameters(String title, String releasedYear, Integer limit, Integer page,
                                                String orderBy, String direction) {
        validationService.validateTitle(title);
        validationService.validateReleasedYear(releasedYear);
        validationService.validateLimit(limit);
        validationService.validatePage(page);
        validationService.validateOrderBy(orderBy);
        validationService.validateDirection(direction);
    }

    /**
     * Searches for a movie by its ID.
     * @param personId the ID of the movie to search for
     * @return the movie that matches the given ID
     */
//    @Override
//    public List<MovieSearchResultDTO> searchMovieByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction) {
//        // Validate if personId exists in database
//        if (!personRepository.existsById(personId)){
//            throw new ResourceNotFoundException(PERSON_ID_NOT_FOUND_CODE, PERSON_ID_NOT_FOUND_MESSAGE);
//        }
//
//        return movieRepository.searchMoviesByPersonId(personId, limit, page, orderBy, direction);
//    }


//    /**
//     * Converts a list of raw query result objects into a list of MovieSearchResultDTOs.
//     * @param results The query results returned from the search
//     * @return returns a list of MovieSearchResultDTO
//     */
//    private List<MovieSearchResultDTO> mapResultsToDTOs(List<Object[]> results) {
//        List<MovieSearchResultDTO> dtoList = new ArrayList<>();
//
//        for (Object[] result : results) {
//            String id = (String) result[0];
//            String movieTitle = (String) result[1];
//            String releaseTime = (String) result[2];
//            String directors = (String) result[3];
//            String backdropPath = (String) result[4];
//            String posterPath = (String) result[5];
//
//            MovieSearchResultDTO dto = new MovieSearchResultDTO(
//                    id,
//                    movieTitle,
//                    releaseTime,
//                    directors,
//                    backdropPath,
//                    posterPath
//            );
//
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
}
