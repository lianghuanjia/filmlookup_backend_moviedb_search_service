package com.example.movie_service.service;


import com.example.movie_service.exception.ValidationException;

public interface ValidationService {
    void validateTitle(String title) throws ValidationException;

    void validateReleasedYear(String releasedYear) throws ValidationException;

    void validateLimit(Integer limit) throws ValidationException;

    void validatePage(Integer page) throws ValidationException;

    void validateOrderBy(String orderBy) throws ValidationException;

    void validateDirection(String direction) throws ValidationException;
}
