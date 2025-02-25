package com.example.movie_service.entity.movie;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * This is the entity that represents Composite Primary Key of the MovieCrew entity
 */
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
