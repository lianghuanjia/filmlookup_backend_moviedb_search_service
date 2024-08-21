package com.example.movie_service.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


import java.io.Serializable;

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
