package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="title_basics")
public class Movie {


    @Id
    @Column(name="tconst")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //need to modify
    private String id;

    @Column(name="titleType")
    private String titleType;

    @Column(name="primaryTitle")
    private String primaryTitle;

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

    @Column(name="genres")
    private String genres;

    @Column(name="backdrop_path")
    private String backdrop_path;

    @Column(name="poster_path")
    private String poster_path;

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
}
