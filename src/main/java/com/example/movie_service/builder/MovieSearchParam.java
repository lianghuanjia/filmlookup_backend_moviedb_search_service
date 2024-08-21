package com.example.movie_service.builder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
