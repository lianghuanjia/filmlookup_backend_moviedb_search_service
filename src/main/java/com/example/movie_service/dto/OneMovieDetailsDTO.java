package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String backdropPath;
    private String posterPath;
    private String directors;
    private List<CrewMember> crewMemberList;
    private Double rating;
    private Integer numOfVotes;
    private List<String> otherNameList;
    private List<String> genreList;
}
