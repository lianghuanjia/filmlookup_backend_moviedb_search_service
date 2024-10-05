package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Use this class to contain the information of each movie crew member in the result of searching a single movie and
 * its details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrewMember {
    private String personId;
    private String name;
    private String profilePath;
    private String jobs;
}
