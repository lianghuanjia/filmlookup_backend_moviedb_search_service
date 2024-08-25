package com.example.movie_service.entity.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the entity that represents movie_rating table
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}
