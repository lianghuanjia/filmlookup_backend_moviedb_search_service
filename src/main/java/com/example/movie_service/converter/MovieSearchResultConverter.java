package com.example.movie_service.converter;

import com.example.movie_service.dto.MovieSearchResultDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieSearchResultConverter implements Converter<Object[], MovieSearchResultDTO>{

    @Override
    public MovieSearchResultDTO convert(Object[] source) {
        String id = (String) source[0];
        String movieTitle = (String) source[1];
        String releaseTime = (String) source[2];
        String directors = (String) source[3];
        String backdropPath = (String) source[4];
        String posterPath = (String) source[5];

        return new MovieSearchResultDTO(id, movieTitle, releaseTime, directors, backdropPath, posterPath);
    }

    public List<MovieSearchResultDTO> convertList(List<Object[]> sourceList) {
        List<MovieSearchResultDTO> dtoList = new ArrayList<>();
        for (Object[] source : sourceList) {
            dtoList.add(convert(source));
        }
        return dtoList;
    }
}
