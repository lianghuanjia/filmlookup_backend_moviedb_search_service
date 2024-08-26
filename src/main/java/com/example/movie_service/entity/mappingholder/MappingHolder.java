package com.example.movie_service.entity.mappingholder;

import com.example.movie_service.dto.MovieSearchResultDTO;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;


import static com.example.movie_service.constant.MovieConstant.MOVIE_SEARCH_RESULT_DTO_MAPPING;

/**
 * This entity is used to centrally manage the @SqlResultSetMappings
 */
@Entity
@SqlResultSetMapping(
        name = MOVIE_SEARCH_RESULT_DTO_MAPPING,
        classes = @ConstructorResult(
                targetClass = MovieSearchResultDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "releaseTime", type = String.class),
                        @ColumnResult(name = "directors", type = String.class),
                        @ColumnResult(name = "backdropPath", type = String.class),
                        @ColumnResult(name = "posterPath", type = String.class),
                        @ColumnResult(name = "rating", type = Double.class),
                }
        )

)

@SuppressWarnings({"unused"})
public class MappingHolder {
    @Id
    private int id;
}
