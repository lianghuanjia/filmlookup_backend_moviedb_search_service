package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchWithTitleDTOFromRepoToService {
    private int totalItem;
    private List<MovieTitleSearchSQLQueryResultDTO> movies;
}
