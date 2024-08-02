package com.example.movie_service.entity;

import com.example.movie_service.generator.CustomTitleIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name="people")
public class People {

    @Id
    @GenericGenerator(name = "custom-name-id", type = CustomTitleIdGenerator.class)
    @GeneratedValue(generator = "custom-name-id")
    @Column(name="nconst")
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
