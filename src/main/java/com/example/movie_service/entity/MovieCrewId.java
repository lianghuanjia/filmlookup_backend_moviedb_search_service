package com.example.movie_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MovieCrewId implements Serializable {
    private String tconst;
    private String nconst;
    private int orderIndex;


    // Figure this out
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieCrewId that = (MovieCrewId) o;
        return orderIndex == that.orderIndex && Objects.equals(tconst, that.tconst) && Objects.equals(nconst, that.nconst);
    }

    // Figure this out
    @Override
    public int hashCode() {
        return Objects.hash(tconst, nconst, orderIndex);
    }
}
