package com.example.movie_service.entity.movie;

import jakarta.persistence.Column;
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
