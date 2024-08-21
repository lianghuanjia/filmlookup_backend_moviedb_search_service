package com.example.movie_service.builder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true) // Enable toBuilder for making modifications
public class MovieSearchParam {
    private String title;
    private String releasedYear;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;
}
