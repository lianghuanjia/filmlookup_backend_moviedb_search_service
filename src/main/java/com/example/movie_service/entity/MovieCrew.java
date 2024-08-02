package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movie_crew")
public class MovieCrew {

    @EmbeddedId
    private MovieCrewId id;

    @ManyToOne
    @MapsId("tconst")
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private Movie movieId;

    @ManyToOne
    @MapsId("nconst")
    @JoinColumn(name = "nconst", referencedColumnName = "nconst")
    private Person personId;

    @Column(name = "orderIndex", insertable = false, updatable = false)
    private int orderIndex;

    @Column(name = "category")
    private String category;
}
