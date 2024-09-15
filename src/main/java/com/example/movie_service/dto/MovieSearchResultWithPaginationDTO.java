package com.example.movie_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MovieSearchResultWithPaginationDTO {
    private List<MovieSearchResponseDTO> movies;
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPrevPage;
}
