package com.example.movie_service.converter;

import com.example.movie_service.dto.MovieSearchQueryDTO;
import com.example.movie_service.dto.MovieSearchResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieSearchQueryToResponseConverter implements Converter<MovieSearchQueryDTO, MovieSearchResponseDTO> {
    @Override
    public MovieSearchResponseDTO convert(MovieSearchQueryDTO source) {
        return new MovieSearchResponseDTO(
                source.getId(),
                source.getTitle(),
                source.getReleaseTime(),
                source.getPosterPath() != null ? source.getPosterPath() : "default-poster-path",
                source.getRating() != null ? source.getRating() : 0.0
        );
    }
}
