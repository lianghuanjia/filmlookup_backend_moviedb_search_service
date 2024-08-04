package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movie_genres")
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
     *          If we see our database ER diagram, the "N" is on the MovieGenre side, and "1" is on the Movie side
     *
     *  Many:   Refers to the instances of the current entity class that can be associated with a single
     *              instance of the target entity class.
     *              The MovieGenre entity is the "many" side because multiple MovieGenre entries can be associated with
     *              a single Movie
     *          The Movie entity is the "one" side because each Movie can appear once in a MovieGenre entry but can
     *              be part of multiple MovieGenre entries if it is
     *
     *  One:    Refers to the instance of the target entity class that can be associated with multiple instances of
     *              the current entity class.
     *
     * @MapsId("movieId")
     *  It should match the field in the MovieGenreId that represents the primary key of the Movie
     *  In the MovieGenreId, it's private String movieId, so here we put "movieId"
     *
     * @JoinColumn(name = "movie_id")
     *  Ensure this matches the actual column name in your database. In our movie_genres table in database, the column
     *  that stores the movie's id is called movie_id, so here we put "movie_id"
     */
    @ManyToOne
    @MapsId("movieId") // It should match the field in the MovieGenreId that represents the primary key of the Movie
    @JoinColumn(name = "movie_id") // Ensure this matches the actual column name of this entity's representing table in your database
    private Movie movie;

    @ManyToOne
    @MapsId("genreId") // It should match the field in the MovieGenreId that represents the primary key of the Genre
    @JoinColumn(name = "genre_id") // Ensure this matches the actual column name in your database
    private Genre genre;

    /**
     * Custom constructor for creating a MovieGenre instance using Movie and Genre objects.
     * This constructor allows the creation of a MovieGenre entity by directly passing
     * the related Movie and Genre entities, automatically setting up the composite key.
     *
     * Unlike the @AllArgsConstructor provided by Lombok, which initializes all fields
     * (including MovieGenreId) based on the arguments, this constructor constructs the
     * MovieGenreId internally using the primary keys of the provided Movie and Genre objects.
     *
     * @param movie the Movie entity associated with this MovieGenre relationship
     * @param genre the Genre entity associated with this MovieGenre relationship
     *
     * Example usage:
     * <pre>
     * {@code
     * Movie movie = new Movie("tt1234567", "Inception", 2010);
     * Genre genre = new Genre(1, "Science Fiction");
     *
     * MovieGenre movieGenre = new MovieGenre(movie, genre);
     *
     * // The id field is automatically created as a new MovieGenreId("tt1234567", 1)
     * // You can then persist this entity to the database using a repository:
     * // movieGenreRepository.save(movieGenre);
     * }
     * </pre>
     */
    public MovieGenre(Movie movie, Genre genre) {
        this.movie = movie;
        this.genre = genre;
        this.id = new MovieGenreId(movie.getId(), genre.getId());
    }
}
