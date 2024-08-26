package com.example.movie_service.entity.movie;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * This is the entity that represents genre table
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The "genres" refers to the "private Set<Genre> genres;" in the Movie entity
     * FetchType.LAZY is the default type that JPA
     * Behavior:
     * When FetchType.LAZY is specified, the related entities (in this case, Movie entities for each Genre) are not loaded from the database until they are explicitly accessed.
     * Default for Collections:
     * Lazy loading is the default fetch strategy for collections (e.g., Set, List) in JPA
     * Advantages of Lazy Loading:
     * Performance: Reduces initial loading time and memory consumption, as related entities are loaded only when necessary.
     * Efficiency: Particularly beneficial in scenarios where you don't always need to load the entire graph of related entities.
     * Disadvantage of Lazy Loading:
     * N+1 Query Problem
     * LazyInitializationException
     * Debugging Challenges
     * Increased Memory Usage
     * Potential Inconsistencies in Distributed Systems
     * Overhead in Concurrent Environments
     **/
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Movie> movies;

}
