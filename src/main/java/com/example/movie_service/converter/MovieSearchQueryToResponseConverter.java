package com.example.movie_service.converter;

import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.example.movie_service.constant.MovieConstant.DEFAULT_POSTER_PATH;
import static com.example.movie_service.constant.MovieConstant.DEFAULT_RATING_SCORE;

@Component
public class MovieSearchQueryToResponseConverter implements Converter<MovieSearchQueryDTO, MovieSearchResponseDTO> {
    @Override
    public MovieSearchResponseDTO convert(MovieSearchQueryDTO source) {
        return new MovieSearchResponseDTO(
                source.getId(),
                source.getTitle(),
                source.getReleaseTime(),
                source.getPosterPath() != null ? source.getPosterPath() : DEFAULT_POSTER_PATH,
                source.getRating() != null ? source.getRating() : DEFAULT_RATING_SCORE,
                source.getOverview()
        );
    }
}
