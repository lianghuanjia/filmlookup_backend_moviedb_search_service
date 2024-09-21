package com.example.movie_service.entity.mappingholder;

import com.example.movie_service.dto.CrewMember;
import com.example.movie_service.dto.MovieTitleSearchSQLQueryResultDTO;
import com.example.movie_service.dto.OneMovieDetailsDTO;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;


import static com.example.movie_service.constant.MovieConstant.MOVIE_TITLE_SEARCH_QUERY_RESULT_DTO_MAPPING;
import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING;
import static com.example.movie_service.constant.MovieConstant.SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING;

/**
 * This entity is used to centrally manage the @SqlResultSetMappings
 */

@SqlResultSetMapping(
        name = MOVIE_TITLE_SEARCH_QUERY_RESULT_DTO_MAPPING,
        classes = @ConstructorResult(
                targetClass = MovieTitleSearchSQLQueryResultDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "releaseTime", type = String.class),
                        @ColumnResult(name = "directors", type = String.class),
                        @ColumnResult(name = "backdropPath", type = String.class),
                        @ColumnResult(name = "posterPath", type = String.class),
                        @ColumnResult(name = "rating", type = Double.class),
                        @ColumnResult(name = "overview", type = String.class),
                }
        )

)

@SqlResultSetMapping(
        name = SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING,
        classes = @ConstructorResult(
                targetClass = OneMovieDetailsDTO.class,
                columns = {
                        // The name should match the field's name in the SELECT statement.
                        // If it's SELECT id ..., then we put name = "id".
                        // If it's SELECT movie_id ..., then we put name = "movie_id".
                        // If it's SELECT movie_id AS id, then we put name = "id".
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "releaseTime", type = String.class),
                        @ColumnResult(name = "budget", type = Long.class),
                        @ColumnResult(name = "revenue", type = Long.class),
                        @ColumnResult(name = "overview", type = String.class),
                        @ColumnResult(name = "tagline", type = String.class),
                        @ColumnResult(name = "runtimeMinutes", type = Integer.class),
                        @ColumnResult(name = "backdropPath", type = String.class),
                        @ColumnResult(name = "posterPath", type = String.class),
                        @ColumnResult(name = "rating", type = Double.class),
                        @ColumnResult(name = "numOfVotes", type = Integer.class),
                        @ColumnResult(name = "otherNames", type = String.class), // If it's a delimited string in the query result
                        @ColumnResult(name = "genres", type = String.class)
                }
        )
)

@SqlResultSetMapping(
        name = SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING,
        classes = @ConstructorResult(
                targetClass = CrewMember.class,
                columns = {
                        @ColumnResult(name = "person_id", type = String.class),
                        @ColumnResult(name = "person_name", type = String.class),
                        @ColumnResult(name = "profilePath", type = String.class),
                        @ColumnResult(name = "jobs", type = String.class)
                }
        )
)

@Entity
@SuppressWarnings({"unused"})
public class MappingHolder {
    @Id
    private int id;
}

