package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieDirector {

    @EmbeddedId
    private MovieDirectorId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @MapsId("directorId")
    @JoinColumn(name = "director_id")
    private People director;

    public MovieDirector(Movie movie, People director) {
        this.movie = movie;
        this.director = director;
        this.id = new MovieDirectorId(movie.getId(), director.getId());
    }
}