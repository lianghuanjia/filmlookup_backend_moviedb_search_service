package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "movie_crew")
public class MovieCrew {

    /**
     * For the field,
     */

    @EmbeddedId // Use the embedded ID to represent the composite key
    private MovieCrewId id;

    @ManyToOne
    @MapsId("movieId") // "movieId" refers to the "private String movieId;" field in the MovieCrewId
    // the movieId key refers to the foreign key column in the movie_crew table, and the referencedColumnName is the primary key
    // the movie_crew's foreign key refers to, which is the movie_id column in the movie tabl
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @MapsId("personId") // "personId" refers to the "private String personId;" field in the MovieCrewId
    // name="person_id" refers the person_id in the movie_crew table
    // referencedColumnName = "person_id" refers the person_id column in the person table
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", insertable = false, updatable = false)
    private Person person;


    @Column(name = "job", insertable = false, updatable = false)
    private String job;

    // Constructor for Movie, Person, and Job, and construction of the composite id. This cannot be done with the
    // Lombok's @AllArgsConstructor
    public MovieCrew(Movie movie, Person person, String job) {
        this.movie = movie;
        this.person = person;
        this.job = job;
        this.id = new MovieCrewId(movie.getId(), person.getId(), job); //
    }
}
