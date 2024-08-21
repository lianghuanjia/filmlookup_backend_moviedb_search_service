package com.example.movie_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The "genres" refers to the "private Set<Genre> genres;" in the Movie entity
     * FetchType.LAZY is the default type that JPA
     *      Behavior:
     *          When FetchType.LAZY is specified, the related entities (in this case, Movie entities for each Genre) are not loaded from the database until they are explicitly accessed.
     *      Default for Collections:
     *          Lazy loading is the default fetch strategy for collections (e.g., Set, List) in JPA
     *      Advantages of Lazy Loading:
     *          Performance: Reduces initial loading time and memory consumption, as related entities are loaded only when necessary.
     *          Efficiency: Particularly beneficial in scenarios where you don't always need to load the entire graph of related entities.
     *      Disadvantage of Lazy Loading:
     *          N+1 Query Problem
     *          LazyInitializationException
     *          Debugging Challenges
     *          Increased Memory Usage
     *          Potential Inconsistencies in Distributed Systems
     *          Overhead in Concurrent Environments
     **/
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Movie> movies;

}
