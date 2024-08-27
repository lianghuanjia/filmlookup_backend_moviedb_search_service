package com.example.movie_service.repository;

import com.example.movie_service.builder.MovieSearchParam;
import com.example.movie_service.dto.CrewMember;
import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.example.movie_service.constant.MovieConstant.MOVIE_SEARCH_RESULT_DTO_MAPPING;
import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING;
import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING;

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
        String sqlQuery = buildQueryStringToSearchMovieWithTitleAndOtherFields(movieSearchParam.getReleasedYear(), movieSearchParam.getDirector(),
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
     * Search one movie's detailed information with its id.
     * @param movieId the movie's id
     * @return
     * Note: Since there are a lot of joining, this will be a complicated query, so I will use
     *       native sql query because compared to HQL, In some cases Hibernate does not generate the most
     *       efficient statements, so then native SQL can be faster
     */
    @Override
    public OneMovieDetailsDTO searchOneMovieDetails(String movieId) {
        // Get a movie's basic details
        OneMovieDetailsDTO singleMovieBasicDetails = getOneMovieBasicDetails(movieId);
        // Get a movie's crew members
        List<CrewMember> crewMembers = getCrewMembersWithProfilePic(movieId);
        singleMovieBasicDetails.setCrewMemberList(crewMembers);

        return singleMovieBasicDetails;
    }

    /**
     * Get a movie's basic details.
     * Details including: movie's id, title, releaseTime, budget, revenue, overview, tagline, runtimeMinutes,
     *                    backdropPath, posterPath, rating, number of votes, this movie's other names, and genres.
     * @param movieId the movie's id
     * @return OneMovieDetailsDTO
     */
    private OneMovieDetailsDTO getOneMovieBasicDetails(String movieId) {
        // Build a query string.
        String sqlQueryString = buildQueryStringToSearchOneMovieBasicDetails();

        // Create a native SQL Query with mapping
        Query query = entityManager.createNativeQuery(sqlQueryString, SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING);

        // Set the movieId into the query. The setParameter also prevents SQL injection, because the input is safely
        // bound to the query as a parameter, and the database treats it as a value rather than part of the SQL
        // statement. This prevents any injected SQL from being executed.
        query.setParameter("movieId", movieId);

        return (OneMovieDetailsDTO) query.getSingleResult();
    }

    /**
     * Use it to get a movie's crew members that are recorded as director, actor, or actress in the database, and they
     * have profile picture path in the database.
     * @param movieId the movie's id
     * @return a list of CrewMember object
     */
    private List<CrewMember> getCrewMembersWithProfilePic(String movieId) {
        String queryString = buildQueryStringToSearchOneMovieCrewMembers();
        Query query = entityManager.createNativeQuery(queryString, SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING);
        query.setParameter("movieId", movieId);
        try {
            @SuppressWarnings("unchecked")
            List<CrewMember> results = query.getResultList();
            if(results.isEmpty()) {
                return Collections.emptyList();
            }
            return results;
        }catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Build a query string to search a movie's crew members that are director, actor, or actress, and they have
     * profile picture path in the database.
     * @return a String.
     */
    private String buildQueryStringToSearchOneMovieCrewMembers() {
        return "SELECT " +
                "mc.person_id AS person_id, " +
                "p.name AS person_name, " +
                "p.profile_path AS profilePath, " +
                "mc.job " +
                "FROM movie_crew mc " +
                "INNER JOIN person p ON mc.person_id = p.person_id AND p.profile_path IS NOT NULL " + // Only get people who have profile_path
                "AND mc.job IN ('Director', 'Actor', 'Actress') " + // Only get director(s), actors, actresses
                "WHERE mc.movie_id = :movieId ";
    }

    /**
     * Build a query string to search a movie's basic details. Details including id, title, releaseTime, budget, revenue,
     * overview, tagline, runtimeMinutes, backdropPath, posterPath, rating, number of votes, the movie's other names,
     * and genres.
     * @return a string.
     */
    private String buildQueryStringToSearchOneMovieBasicDetails() {
        return "SELECT " +

                // Get movie_id, primaryTitle, releasedTime, budget, revenue, overview, tagline, runtimeMinutes, backdropPath,
                // posterPath from the movie table
                "m.movie_id AS id, " +
                "m.primaryTitle AS title, " +
                "m.releaseTime, " +
                "m.budget, " +
                "m.revenue, " +
                "m.overview, " +
                "m.tagline, " +
                "m.runtimeMinutes, " +
                "m.backdrop_path AS backdropPath, " +
                "m.poster_path AS posterPath, " +

                // Get average rating and number of votes from movie_rating table
                "mr.averageRating AS rating, " +
                "mr.numVotes AS numOfVotes, " +

                // Get a movie's other unique names from movie_akas table, and get its genres from genre table
                // GROUP_CONCAT aggregates multiple values into one single string.
                "GROUP_CONCAT(DISTINCT ma.title) AS otherNames, " +
                "GROUP_CONCAT(DISTINCT g.name) AS genres " +
                "FROM movie m " +

                // Left join other tables
                "LEFT JOIN movie_rating mr ON m.movie_id = mr.movie_id " +
                "LEFT JOIN movie_akas ma ON m.movie_id = ma.movie_id " +
                "LEFT JOIN movie_genres mg ON m.movie_id = mg.movie_id " +
                "LEFT JOIN genre g ON mg.genre_id = g.id " +

                // Set the condition
                "WHERE m.movie_id = :movieId " +

                // GROUP BY m.movie_id: We want to retrieve details for a specific movie using its movie_id. Grouping by movie_id
                // ensures that all related data (e.g., crew members, ratings, genres, etc.) is associated with that single movie.
                // GROUP BY mc.person_id, mc.job: Each movie can have multiple crew members, and each crew member can have
                // different jobs. Grouping by person_id and job ensures that you get a distinct row for each crew member and their job for the given movie_id.
                "GROUP BY m.movie_id";
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
    private String buildQueryStringToSearchMovieWithTitleAndOtherFields(String releasedYear, String director, String genre
                                                                            , String orderBy, String direction) {
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
