package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext //This is specific for EntityManager that provides Transaction-Scoped Behavior
    private EntityManager entityManager;

    @Override
    public List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction) {
        List<MovieSearchResultDTO> resultDTOS = buildAndExecuteQuery(title, releasedYear, director, genre, limit, page,  orderBy, direction);

        return resultDTOS;
    }



    private List<MovieSearchResultDTO> buildAndExecuteQuery(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction) {
        String jpql = "SELECT DISTINCT m FROM Movie m "+
                "LEFT JOIN FETCH m.movieCrews movieCrew " +
                "LEFT JOIN FETCH movieCrew.person movieCrewPerson "+
                "LEFT JOIN FETCH m.genres genre "+
                "WHERE m.title LIKE :title ";

        // Create a map to store parameter values
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("title", "%" + title + "%"); // Use wildcard %% to treat the title to be a substring to search


        // Add additional conditions dynamically
        if (releasedYear != null && !releasedYear.isEmpty()) {
            jpql += "AND m.releaseTime LIKE :releasedYear ";
            parameters.put("releasedYear", releasedYear+"%"); // Use the % at the end to look for releasedYear that starts with the given parameter releasedYear
        }
        if (director != null && !director.isEmpty()) {
            jpql += "AND movieCrewPerson.name LIKE :director ";
            parameters.put("director", "%"+director+"%");

        }
        if (genre != null && !genre.isEmpty()) {
            jpql += "AND genre.name LIKE :genre ";
            parameters.put("genre", "%"+genre+"%");
        }

        // Add the order by and direction. They have default value, so they always have values and are not null
        jpql += "ORDER BY m." + orderBy + " " + direction;


        TypedQuery<Movie> query = entityManager.createQuery(jpql, Movie.class);

        // Set all parameters that are present in the parameters map into the query:
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        // set the offset and max results return each time
        int offSet = (page-1) * limit;
        query.setFirstResult(offSet);
        query.setMaxResults(limit);


        List<Movie> movies = query.getResultList();
        // Execute the query and return the results

        List<MovieSearchResultDTO> dtoList = new ArrayList<>();


        for (Movie movie: movies) {
            // Get director(s)'s name of those movies and concatenate them using "," if there are more than 1 director of one movie
            String directors = movie.getMovieCrews().stream()
                    .filter(mc -> "director".equals(mc.getJob()))
                    .map(mc -> mc.getPerson().getName())
                    .distinct()
                    .sorted()
                    .collect(Collectors.joining(","));

            // Map the information we collect so far into MovieSearchResultDTO
            MovieSearchResultDTO dto = new MovieSearchResultDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getReleaseTime(),
                    movie.getBackdropPath(),
                    movie.getPosterPath(),
                    directors
            );

            // Add the created MovieSearchResultDTO into dtoList
            dtoList.add(dto);
        }

        return dtoList;
    }


    /**
     * Sets the parameters for a database query based on the provided input criteria.
     * @param query         The query object to set parameters on.
     * @param title         The title of the movie to search for. Wildcards (%) are added for partial matching
     * @param releasedYear  The released year of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param director      The director of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param genre         The genre of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param limit         The maximum number of results to return (for pagination).
     * @param page          The page number of results to return, used to calculate the offset.
     *
     * Assumes that the query object is a valid and non-null instance of a query with named parameters.
     */
    private void setQueryParameters(Query query, String title, String releasedYear, String director, String genre, int limit, int page) {
        query.setParameter("title", "%" + title + "%");
        query.setParameter("limit", limit);
        query.setParameter("offset", page * limit);

        if (releasedYear != null && !releasedYear.isEmpty()) {
            query.setParameter("releasedYear", "%" + releasedYear + "%");
        }
        if (director != null && !director.isEmpty()) {
            query.setParameter("director", "%" + director + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            query.setParameter("genre", "%" + genre + "%");
        }
    }


    /**
     * Converts a list of raw query result objects into a list of MovieSearchResultDTOs.
     * @param results The query results returned from the search
     * @return
     */
    private List<MovieSearchResultDTO> mapResultsToDTOs(List<Object[]> results) {
        List<MovieSearchResultDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            String id = (String) result[0];
            String movieTitle = (String) result[1];
            String releaseTime = (String) result[2];
            String directors = (String) result[3];
            String backdropPath = (String) result[4];
            String posterPath = (String) result[5];

            MovieSearchResultDTO dto = new MovieSearchResultDTO(
                    id,
                    movieTitle,
                    releaseTime,
                    directors,
                    backdropPath,
                    posterPath
            );

            dtoList.add(dto);
        }
        return dtoList;
    }


    @Override
    public List<MovieSearchResultDTO> searchMoviesByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction) {
        // JPQL Query to find movies by person ID with sorting
        String jpqlQuery = "SELECT DISTINCT m FROM Movie m " +
                "JOIN FETCH m.movieCrews mc " +
                "WHERE mc.person.id = :personId " +
                "ORDER BY m." + orderBy + " " + direction;

        // Create the query and set the parameter
        TypedQuery<Movie> query = entityManager.createQuery(jpqlQuery, Movie.class);
        query.setParameter("personId", personId);

        // Apply pagination
        query.setFirstResult(page * limit);
        query.setMaxResults(limit);

        // Execute the query and get the results
        List<Movie> movies = query.getResultList();

        // Map the results to MovieSearchResultDTO
        return movies.stream()
                .map(movie -> {
                    // Extract director names from movieCrews
                    String directors = movie.getMovieCrews().stream()
                            .filter(crew -> "director".equalsIgnoreCase(crew.getJob()))
                            .map(crew -> crew.getPerson().getName())
                            .collect(Collectors.joining(", "));

                    return new MovieSearchResultDTO(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getReleaseTime(),
                            directors,
                            movie.getBackdropPath(),
                            movie.getPosterPath()
                    );
                })
                .collect(Collectors.toList());
    }


    private void printMappedDtoResults(List<MovieSearchResultDTO> dtoList) {
        System.out.println("Mapped DTO Results:");
        for (MovieSearchResultDTO dto : dtoList) {
            System.out.println(dto.toString());
            System.out.println("------------");
        }
    }


}
