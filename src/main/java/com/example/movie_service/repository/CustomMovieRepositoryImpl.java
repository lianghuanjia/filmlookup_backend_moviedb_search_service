package com.example.movie_service.repository;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.MovieSearchResultDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.movie_service.constant.MovieConstant.MOVIE_SEARCH_RESULT_DTO_MAPPING;

/**
 * Implementation of the Interface of custom movie repository layer
 */
@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    // @PersistenceContext is specific for EntityManager that provides Transaction-Scoped Behavior
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Search movies that meets the criteria of the parameters inside the movieSearchParam from MySQL database
     * @param movieSearchParam a class that encapsulates all necessary parameters to search movies
     * @return A list of MovieSearchResultDTO. Each MovieSearchResultDTO represents one searched movie result
     */
    @Override
    public List<MovieSearchResultDTO> searchMovies(MovieSearchParam movieSearchParam) {
        // Build string query with optional parameters
        String sqlQuery = buildSqlQuery(movieSearchParam.getReleasedYear(), movieSearchParam.getDirector(),
                movieSearchParam.getGenre(), movieSearchParam.getOrderBy(), movieSearchParam.getDirection());

        // Create bare-bones native SQL Query
        Query query = entityManager.createNativeQuery(sqlQuery, MOVIE_SEARCH_RESULT_DTO_MAPPING);

        // Set the query's parameters based on the function's parameters
        setQueryParameters(query, movieSearchParam.getTitle(), movieSearchParam.getReleasedYear(),
                movieSearchParam.getDirector(), movieSearchParam.getGenre(), movieSearchParam.getLimit(),
                movieSearchParam.getPage());

        // Get the result(s) from entityManager.getResultList(). Each result will be mapped to MovieSearchResultDTO.
        // Since the MOVIE_SEARCH_RESULT_DTO_MAPPING's target class is MovieSearchResultDTO, I am sure the result will
        // be MovieSearchResultDTO class, so I use @SuppressWarnings("unchecked")  here
        @SuppressWarnings("unchecked")
        List<MovieSearchResultDTO> results = query.getResultList();
        return results;
    }

    /**
     * Use parameters to build a query string
     * @param releasedYear The released year of a movie. Can be Nullable or an empty String.
     * @param director The movie's director. Can be Nullable or an empty String.
     * @param genre The movie's genre. Can be Nullable or an empty String.
     * @param orderBy The field that order the result. By default, it's "title", it can also be "rating" or "releaseTime".
     * @param direction The direction of the ordered results. By default, it's "asc". It can also be "desc".
     * @return A query string
     */
    private String buildSqlQuery(String releasedYear, String director, String genre, String orderBy, String direction) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT m.movie_id AS id, " +
                        "m.primaryTitle AS title, " +
                        "m.releaseTime AS releaseTime, " +
                        "GROUP_CONCAT(DISTINCT p.name ORDER BY p.name SEPARATOR ', ') AS directors, " +
                        "m.backdrop_path AS backdropPath, " +
                        "m.poster_path AS posterPath, " +
                        "mr.averageRating AS rating " +
                        "FROM movie m " +
                        "LEFT JOIN movie_crew mc ON m.movie_id = mc.movie_id " +
                        "LEFT JOIN person p ON mc.person_id = p.person_id AND mc.job = 'director' " +
                        "LEFT JOIN movie_genres mg ON m.movie_id = mg.movie_id " +
                        "LEFT JOIN genre g ON mg.genre_id = g.id " +
                        "LEFT JOIN movie_rating mr ON m.movie_id = mr.movie_id " +
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
     *
     * @param query        The query object to set parameters on.
     * @param title        The title of the movie to search for. Wildcards (%) are added for partial matching
     * @param releasedYear The released year of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param director     The director of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param genre        The genre of the movie. Wildcards (%) are added for partial matching. Can be null.
     * @param limit        The maximum number of results to return (for pagination).
     * @param page         The page number of results to return, used to calculate the offset.
     *                     <p>
     *                     Assumes that the query object is a valid and non-null instance of a query with named parameters.
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

}
