package com.example.movie_service.entity;

import com.example.movie_service.generator.CustomTitleIdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // With Builder, you can easily handle optional fields without creating numerous constructors. You only set the fields that are relevant for a particular object creation.
@Table(name="movie")
public class Movie {


    @Id
    @Column(name="movie_id")
    @GenericGenerator(name = "custom-title-id", type = CustomTitleIdGenerator.class)
    @GeneratedValue(generator = "custom-title-id")
    private String id;

    @Column(name="titleType")
    private String titleType;

    @Column(name="primaryTitle") // Ensure this matches the column name exactly
    private String title;

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

    @Column(name="backdrop_path")
    private String backdropPath;

    @Column(name="poster_path")
    private String posterPath;

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

    @ManyToMany // One Movie can have multiple Genres, and one Genre can have multiple Movies. So many-to-many relationship
    @JoinTable( // We only need to define this JoinTable on the owning side, which is here
            name = "movie_genres", // It refers to the mapping table in our database that maps the Movie and Genre relationship.
            joinColumns = @JoinColumn(name = "movie_id"), // It refers to the movie_id column in the movie_genres table.
            inverseJoinColumns = @JoinColumn(name = "genre_id") // movie_genres's genre_id refers to other entity
    )
    // We can use this field to get all the Genre entities that this Movie has
    // Also this field refers to the mappedBy in the Genre entity
    private Set<Genre> genres;


    // the mappedBy attribute is used in the @OneToMany and @ManyToMany annotations to specify the field in the other
    // entity that owns the relationship. In the MovieCrew, it's the "private Movie movie;" that owns the relationship.
    // If it's "private Movie theMovie" in the MovieCrew entity, the following mappedBy = "movie" will become "theMovie"
    // cascade = CascadeType.ALL means all operations (such as persist, merge, remove, refresh, detach) performed on the
    // parent entity should be automatically propagated to the associated child entities
    // FetchType.LAZY means the movieCrews won't be fetched until we are using it, i.e. call its getter method.
    // When we call its getter, it will make a query to the database to ge the MovieCrew associated with this Movie
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MovieCrew> movieCrews;

    @OneToMany(mappedBy="movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MovieAkas> movieAkas;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MovieRating movieRating;

}
