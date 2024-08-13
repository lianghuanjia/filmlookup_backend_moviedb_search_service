package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.movie_service.constant.MovieConstant.MOVIE_SEARCH_RESULT_DTO_MAPPING;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext //This is specific for EntityManager that provides Transaction-Scoped Behavior
    private EntityManager entityManager;

    @Override
    public List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit,
                                       Integer page, String orderBy, String direction) {
        String sqlQuery = buildSqlQuery(releasedYear, director, genre, orderBy, direction);

        // Create bare-bones native SQL Query
        Query query = entityManager.createNativeQuery(sqlQuery, MOVIE_SEARCH_RESULT_DTO_MAPPING);

        // Set the query's parameters based on the function's parameters
        setQueryParameters(query, title, releasedYear, director, genre, limit, page);

        // Get the result(s) from entityManager.getResultList(). Each result will be mapped to MovieSearchResultDTO.
        // Since the MOVIE_SEARCH_RESULT_DTO_MAPPING's target class is MovieSearchResultDTO, I am sure the result will
        // be MovieSearchResultDTO class, so I use @SuppressWarnings("unchecked")  here
        @SuppressWarnings("unchecked")
        List<MovieSearchResultDTO> results = query.getResultList();

        return results;
    }

    private String getQueryWithParameters(String sqlQuery, String title, String releasedYear, String director, String genre, int limit, int page) {
        sqlQuery = sqlQuery.replace(":title", "'%" + title + "%'");
        sqlQuery = sqlQuery.replace(":limit", Integer.toString(limit));
        sqlQuery = sqlQuery.replace(":offset", Integer.toString((page) * limit));  // Corrected offset calculation

        if (releasedYear != null && !releasedYear.isEmpty()) {
            sqlQuery = sqlQuery.replace(":releasedYear", "'%" + releasedYear + "%'");
        }
        if (director != null && !director.isEmpty()) {
            sqlQuery = sqlQuery.replace(":director", "'%" + director + "%'");
        }
        if (genre != null && !genre.isEmpty()) {
            sqlQuery = sqlQuery.replace(":genre", "'%" + genre + "%'");
        }

        return sqlQuery;
    }

    private String buildSqlQuery(String releasedYear, String director, String genre, String orderBy, String direction) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT m.movie_id AS id, " +
                        "m.primaryTitle AS title, " +
                        "m.releaseTime AS releaseTime, " +
                        "GROUP_CONCAT(DISTINCT p.name ORDER BY p.name SEPARATOR ', ') AS directors, " +
                        "m.backdrop_path AS backdropPath, " +
                        "m.poster_path AS posterPath " +
                        "FROM movie m " +
                        "LEFT JOIN movie_crew mc ON m.movie_id = mc.movie_id " +
                        "LEFT JOIN person p ON mc.person_id = p.person_id AND mc.job = 'director' " +
                        "LEFT JOIN movie_genres mg ON m.movie_id = mg.movie_id " +
                        "LEFT JOIN genre g ON mg.genre_id = g.id " +
                        "WHERE m.primaryTitle LIKE :title "
        );

        if (releasedYear != null && !releasedYear.isEmpty()) {
            queryBuilder.append("AND m.releaseTime LIKE :releasedYear ");
        }
        if (director != null && !director.isEmpty()) {
            queryBuilder.append("AND p.name LIKE :director ");
        }
        if (genre != null && !genre.isEmpty()) {
            queryBuilder.append("AND g.name LIKE :genre ");
        }

        queryBuilder.append("GROUP BY m.movie_id, m.primaryTitle, m.releaseTime, m.backdrop_path, m.poster_path ")
                .append("ORDER BY ").append(orderBy).append(" ").append(direction).append(" ")
                .append("LIMIT :limit OFFSET :offset");

        return queryBuilder.toString();
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
            query.setParameter("releasedYear", releasedYear + "%");
        }
        if (director != null && !director.isEmpty()) {
            query.setParameter("director", "%" + director + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            query.setParameter("genre", "%" + genre + "%");
        }
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
