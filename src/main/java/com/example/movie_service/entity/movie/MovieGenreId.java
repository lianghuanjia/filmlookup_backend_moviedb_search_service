package com.example.movie_service.entity.movie;

import jakarta.persistence.Embeddable;
import lombok.*;


import java.io.Serializable;

/**
 * This is the entity that represents Composite Primary Key of the MovieGenre entity
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovieGenreId implements Serializable {

    private String movieId;
    private int genreId;
}
