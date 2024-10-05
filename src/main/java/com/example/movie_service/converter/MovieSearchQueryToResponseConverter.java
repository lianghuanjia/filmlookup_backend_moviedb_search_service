package com.example.movie_service.converter;

import com.example.movie_service.dto.MovieTitleSearchSQLQueryResultDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.example.movie_service.constant.MovieConstant.DEFAULT_POSTER_PATH;

@Component
public class MovieSearchQueryToResponseConverter implements Converter<MovieTitleSearchSQLQueryResultDTO, MovieSearchResponseDTO> {
    @Override
    public MovieSearchResponseDTO convert(MovieTitleSearchSQLQueryResultDTO source) {
        return new MovieSearchResponseDTO(
                source.getId(),
                source.getTitle(),
                source.getReleaseTime(),
                source.getPosterPath() != null ? source.getPosterPath() : DEFAULT_POSTER_PATH,
                source.getRating(),
                source.getOverview()
        );
    }
}
