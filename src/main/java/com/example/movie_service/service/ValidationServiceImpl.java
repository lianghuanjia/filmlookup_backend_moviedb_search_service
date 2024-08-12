package com.example.movie_service.service;

import com.example.movie_service.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.Year;

import static com.example.movie_service.constant.MovieConstant.*;

@Service
public class ValidationServiceImpl implements ValidationService{
    @Override
    public void validateTitle(String title) throws ValidationException {
        if (title == null || title.isEmpty()){
            throw new ValidationException(MISSING_TITLE_CODE, MISSING_TITLE_MESSAGE);
        }
    }

    @Override
    public void validateReleasedYear(String releasedYear) throws ValidationException {
        if (releasedYear != null && Integer.parseInt(releasedYear) > Year.now().getValue()) {
            throw new ValidationException(INVALID_YEAR_CODE, INVALID_YEAR_MESSAGE);
        }
    }

    @Override
    public void validateLimit(Integer limit) throws ValidationException {
        if (limit != 10 && limit != 20 && limit != 30) {
            throw new ValidationException(INVALID_LIMIT_CODE, INVALID_LIMIT_MESSAGE);
        }
    }

    @Override
    public void validatePage(Integer page) throws ValidationException {
        if (page < 0) {
            throw new ValidationException(INVALID_PAGE_CODE, INVALID_PAGE_MESSAGE);
        }
    }

    @Override
    public void validateOrderBy(String orderBy) throws ValidationException {
        if (!orderBy.equals("rating") && !orderBy.equals("title") && !orderBy.equals("releaseTime")) {
            throw new ValidationException(INVALID_ORDER_BY_CODE, INVALID_ORDER_BY_MESSAGE);
        }
    }

    @Override
    public void validateDirection(String direction) throws ValidationException {
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new ValidationException(INVALID_DIRECTION_CODE, INVALID_DIRECTION_MESSAGE);
        }
    }
}
