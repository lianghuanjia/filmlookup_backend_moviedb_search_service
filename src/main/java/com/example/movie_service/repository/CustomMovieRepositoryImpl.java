package com.example.movie_service.repository;

import com.example.movie_service.dto.MovieSearchResultDTO;
import com.example.movie_service.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MovieSearchResultDTO> searchMovies(String title, String releasedYear, String director, String genre, int limit, int page, String orderBy, String direction) {
        StringBuilder queryBuilder = new StringBuilder("SELECT m, d.name " +
                "FROM Movie m " +
                "LEFT JOIN m.directors d " +
                "LEFT JOIN m.genres g " +
                "WHERE m.title LIKE :title ");

        // Add optional parameters
        if (releasedYear != null && !releasedYear.isEmpty()) {
            queryBuilder.append("AND m.releaseTime LIKE :releasedYear ");
        }
        if (director != null && !director.isEmpty()) {
            queryBuilder.append("AND d.name LIKE :director ");
        }
        if (genre != null && !genre.isEmpty()) {
            queryBuilder.append("AND g.name LIKE :genre ");
        }

        // Add sorting
        queryBuilder.append("ORDER BY m.").append(orderBy).append(" ").append(direction);

        TypedQuery<Object[]> query = entityManager.createQuery(queryBuilder.toString(), Object[].class);
        query.setParameter("title", "%" + title + "%");

        if (releasedYear != null && !releasedYear.isEmpty()) {
            query.setParameter("releasedYear", "%" + releasedYear + "%");
        }
        if (director != null && !director.isEmpty()) {
            query.setParameter("director", "%" + director + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            query.setParameter("genre", "%" + genre + "%");
        }

        // Set pagination
        query.setFirstResult(page * limit);
        query.setMaxResults(limit);

        List<Object[]> results = query.getResultList();

        // Process results to remove duplicates and concatenate directors
        Map<String, MovieSearchResultDTO> movieMap = new HashMap<>();
        for (Object[] result : results) {
            Movie movie = (Movie) result[0];
            String directorName = (String) result[1];

            MovieSearchResultDTO dto = movieMap.get(movie.getId());
            if (dto == null) {
                dto = new MovieSearchResultDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseTime(),
                        directorName,  // Start with first director
                        movie.getBackdropPath(),
                        movie.getPosterPath()
                );
                movieMap.put(movie.getId(), dto);
            } else {
                // Concatenate director names
                dto.setDirectors(dto.getDirectors() + ", " + directorName);
            }
        }

        return new ArrayList<>(movieMap.values());
    }

}
