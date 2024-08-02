package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext //This is specific for EntityManager that provides Transaction-Scoped Behavior
    private EntityManager entityManager;

    @Override
    public List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, Integer limit, Integer page, String orderBy, String direction) {
        String sqlQuery = buildSqlQuery(releasedYear, director, genre, orderBy, direction);

        Query query = entityManager.createNativeQuery(sqlQuery);
        setQueryParameters(query, title, releasedYear, director, genre, limit, page);

        List<Object[]> results = query.getResultList();

        return mapResultsToDTOs(results);
    }

    @Override
    public List<MovieSearchResultDTO> searchMoviesByPersonId(String personId, Integer limit, Integer page, String orderBy, String direction) {

        // JPQL Query to find movies by person ID with sorting
        String jpqlQuery = "SELECT m FROM Movie m " +
                "WHERE m.id IN (" +
                "SELECT DISTINCT mc.id.tconst " +
                "FROM MovieCrew mc " +
                "WHERE mc.id.nconst = :personId) " +
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
                .map(movie -> new MovieSearchResultDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseTime(),
                        movie.getDirectors().stream()
                                .map(director -> director.getName())
                                .collect(Collectors.joining(", ")),
                        movie.getBackdropPath(),
                        movie.getPosterPath()
                ))
                .collect(Collectors.toList());
    }

    private String buildSqlQuery(String releasedYear, String director, String genre, String orderBy, String direction) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT m.tconst AS id, " +
                        "m.primaryTitle AS title, " +
                        "m.releaseTime AS releaseTime, " +
                        "GROUP_CONCAT(DISTINCT d.name ORDER BY d.name SEPARATOR ', ') AS directors, " +
                        "m.backdrop_path AS backdropPath, " +
                        "m.poster_path AS posterPath " +
                        "FROM movie m " +
                        "LEFT JOIN movie_directors md ON m.tconst = md.movie_id " +
                        "LEFT JOIN people d ON md.director_id = d.nconst " +
                        "LEFT JOIN movie_genres mg ON m.tconst = mg.movie_id " +
                        "LEFT JOIN genres g ON mg.genre_id = g.id " +
                        "WHERE m.primaryTitle LIKE :title "
        );

        if (releasedYear != null && !releasedYear.isEmpty()) {
            queryBuilder.append("AND m.releaseTime LIKE :releasedYear ");
        }
        if (director != null && !director.isEmpty()) {
            queryBuilder.append("AND d.name LIKE :director ");
        }
        if (genre != null && !genre.isEmpty()) {
            queryBuilder.append("AND g.name LIKE :genre ");
        }

        queryBuilder.append("GROUP BY m.tconst ")
                .append("ORDER BY ").append(orderBy).append(" ").append(direction)
                .append(" LIMIT :limit OFFSET :offset");

        return queryBuilder.toString();
    }

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

        // Debugging: Print mapped DTO results
        printMappedDtoResults(dtoList);

        return dtoList;
    }

    private void printMappedDtoResults(List<MovieSearchResultDTO> dtoList) {
        System.out.println("Mapped DTO Results:");
        for (MovieSearchResultDTO dto : dtoList) {
            System.out.println(dto.toString());
            System.out.println("------------");
        }
    }


}
