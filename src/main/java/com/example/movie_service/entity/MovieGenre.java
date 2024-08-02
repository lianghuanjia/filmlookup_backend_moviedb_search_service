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

    @EmbeddedId
    private MovieGenreId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie_id;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre_id;

    public MovieGenre(Movie movie_id, Genre genre_id) {
        this.movie_id = movie_id;
        this.genre_id = genre_id;
        this.id = new MovieGenreId(movie_id.getId(), genre_id.getId());
    }
}
