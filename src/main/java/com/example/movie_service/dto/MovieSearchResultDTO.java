package com.example.movie_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResultDTO {
    private String id;
    private String title;
    private String releaseTime;
    private String directors;
    private String backdropPath;
    private String posterPath;
    private Double rating;
}
