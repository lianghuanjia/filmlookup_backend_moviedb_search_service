package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "movie_rating")
public class MovieRating {

    @Id
    @Column(name = "movie_id")
    // We don't need to use @GeneratedValue and @GenericGenerator for this id like the id in the Movie entity.
    // Because this movieId doesn't generate itself, instead, it's a foreign key that refers and relies on the
    // id field in the Movie entity. Therefore, Movie entity's id needs the @GeneratedValue and @GenericGenerator,
    // and the movieId here doesn't need it.
    private String movieId;

    @Column(name = "averageRating")
    private Double averageRating;

    @Column(name = "numVotes")
    private Integer numVotes;


    @OneToOne
    @MapsId // When @MapsId is used without a specified field, it defaults to using the primary key of the owning entity
            // (in this case, MovieRating) and maps it directly to the referenced entity (Movie)'s primary key.
    @JoinColumn(name = "movie_id") // "movie_id" refers to the movie_id column in the movie_rating table
    private Movie movie;

    // Constructors, getters, setters
    public MovieRating(Double averageRating, Integer numVotes, Movie movie) {
        this.averageRating = averageRating;
        this.numVotes = numVotes;
        this.movie = movie;
        this.movieId = movie.getId();
    }
}
