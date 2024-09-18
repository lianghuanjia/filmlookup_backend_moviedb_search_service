package com.example.movie_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MovieSearchWithTitleDTOFromRepoToService {
    private int totalItem;
    private List<MovieSearchQueryDTO> movies;
}
