package com.example.movie_service.entity.movie;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the entity that represents movie_genres table
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_genres")
public class MovieGenre {

    /**
     * Composite primary key for the MovieGenre entity.
     * The id field is an instance of MovieGenreId,
     * which contains the composite key (movieId, genreId).
     */
    @EmbeddedId
    private MovieGenreId id;

    /**
     * Many-to-One relationship with the Movie entity.
     * If we see our database ER diagram, the "N" is on the MovieGenre side, and "1" is on the Movie side
     * <p>
     * Many:   Refers to the instances of the current entity class that can be associated with a single
     * instance of the target entity class.
     * The MovieGenre entity is the "many" side because multiple MovieGenre entries can be associated with
     * a single Movie
     * The Movie entity is the "one" side because each Movie can appear once in a MovieGenre entry but can
     * be part of multiple MovieGenre entries if it is
     * <p>
     * One:    Refers to the instance of the target entity class that can be associated with multiple instances of
     * the current entity class.
     * <br>
     * @ MapsId("movieId") It should match the field in the MovieGenreId that represents the primary key of the Movie
     * In the MovieGenreId, it's private String movieId, so here we put "movieId"
     * @ JoinColumn(name = "movie_id")
     * Ensure this matches the actual column name in your database. In our movie_genres table in database, the column
     * that stores the movie's id is called movie_id, so here we put "movie_id"
     */
    @ManyToOne
    @MapsId("movieId") // It should match the field in the MovieGenreId that represents the primary key of the Movie
    @JoinColumn(name = "movie_id")
    // Ensure this matches the actual column name of this entity's representing table in your database
    private Movie movie;

    @ManyToOne
    @MapsId("genreId") // It should match the field in the MovieGenreId that represents the primary key of the Genre
    @JoinColumn(name = "genre_id") // Ensure this matches the actual column name in your database
    private Genre genre;

}
