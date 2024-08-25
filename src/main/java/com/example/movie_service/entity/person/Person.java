package com.example.movie_service.entity.person;

import com.example.movie_service.annotation.CustomIdGeneratorAnnotation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.example.movie_service.constant.MovieConstant.PERSON_ID_PREFIX;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "person_id")
    @CustomIdGeneratorAnnotation(prefix = PERSON_ID_PREFIX)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "deathday")
    private String deathday;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "popularity", precision = 6, scale = 3)
    private BigDecimal popularity;

    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "gender")
    private Integer gender;
}
