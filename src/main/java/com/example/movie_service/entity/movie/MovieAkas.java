package com.example.movie_service.entity.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the entity that represents movie_akas table
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_akas")
public class MovieAkas {

    @EmbeddedId
    private MovieAkasId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    @Column(name = "title")
    private String title;

    @Column(name = "region")
    private String region;

    @Column(name = "isOriginalTitle")
    private Boolean isOriginalTitle;
}
