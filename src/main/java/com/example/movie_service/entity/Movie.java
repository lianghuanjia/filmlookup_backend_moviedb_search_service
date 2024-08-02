package com.example.movie_service.entity;

import com.example.movie_service.generator.CustomTitleIdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movie_basics")
public class Movie {


    @Id
    @Column(name="tconst")
    @GenericGenerator(name = "custom-title-id", type = CustomTitleIdGenerator.class)
    @GeneratedValue(generator = "custom-title-id")
    private String id;

    @Column(name="titleType")
    private String titleType;

    @Column(name="primaryTitle") // Ensure this matches the column name exactly
    private String title;

    @Column(name="originalTitle")
    private String originalTitle;

    @Column(name="isAdult")
    private Byte isAdult;

    @Column(name="releaseTime")
    private String releaseTime;

    @Column(name="endYear")
    private Integer endYear;

    @Column(name="runtimeMinutes")
    private Integer runtimeMinutes;

    @Column(name="backdrop_path")
    private String backdropPath;

    @Column(name="poster_path")
    private String posterPath;

    @Column(name="overview")
    private String overview;

    @Column(name="tagline")
    private String tagline;

    @Column(name="revenue")
    private Long revenue;

    @Column(name="status")
    private String status;

    @Column(name="budget")
    private Long budget;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<People> directors;

    public Movie(String id) {
        this.id = id;
    }

}
