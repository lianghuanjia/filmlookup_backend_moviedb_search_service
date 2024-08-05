package com.example.movie_service.service;

import com.example.movie_service.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class ValidationServiceImpl implements ValidationService{

    @Override
    public void validateTitle(String title) throws ValidationException {
        if (title == null || title.isEmpty()) {
            throw new ValidationException("invalid_title");
        }
    }

    @Override
    public void validateReleasedYear(String releasedYear) throws ValidationException {
        if (releasedYear != null && Integer.parseInt(releasedYear) > Year.now().getValue()) {
            throw new ValidationException("invalid_year");
        }
    }

    @Override
    public void validateLimit(Integer limit) throws ValidationException {
        if (limit != null && (limit != 10 && limit != 20 && limit != 30)) {
            throw new ValidationException("invalid_limit");
        }
    }

    @Override
    public void validatePage(Integer page) throws ValidationException {
        if (page != null && page < 1) {
            throw new ValidationException("invalid_page");
        }
    }

    @Override
    public void validateOrderBy(String orderBy) throws ValidationException {
        if (orderBy != null && !orderBy.equals("rating") && !orderBy.equals("title") && !orderBy.equals("releaseTime")) {
            throw new ValidationException("invalid_orderBy");
        }
    }

    @Override
    public void validateDirection(String direction) throws ValidationException {
        if (direction != null && !direction.equals("asc") && !direction.equals("desc")) {
            throw new ValidationException("invalid_direction");
        }
    }
}
