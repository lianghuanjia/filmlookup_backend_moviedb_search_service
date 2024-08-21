package com.example.movie_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Mark this class as embeddable for use as a composite key
public class MovieCrewId implements Serializable {
    private String movieId;
    private String personId;
    private String job;
}
