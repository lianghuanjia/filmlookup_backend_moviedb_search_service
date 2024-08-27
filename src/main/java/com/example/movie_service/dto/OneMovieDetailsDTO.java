package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneMovieDetailsDTO {
    private String id;
    private String title;
    private String releaseTime;
    private Long budget;
    private Long revenue;
    private String overview;
    private String tagline;
    private Integer runtimeMinutes;
    private String backdropPath;
    private String posterPath;
    private Double rating;
    private Integer numOfVotes;
    private String otherNames; // Other names concatenated in one String with , as delimiter
    private String genres; // All genres concatenated in one String with , as delimiter
    private List<CrewMember> crewMemberList = new ArrayList<>();
}
