package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResponseDTO {
    private String id;
    private String title;
    private String releaseTime;
    private String posterPath;
    private Double rating;
}
