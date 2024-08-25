package com.example.movie_service.service;

import com.example.movie_service.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.Year;

import static com.example.movie_service.constant.MovieConstant.*;

/**
 * Implementation of the ValidationService
 */
@Service
public class ValidationServiceImpl implements ValidationService {

    /**
     * Validate the searching title to make sure it can't be null or an empty String
     * @param title the searching movie title
     * @throws ValidationException ValidationException with code MISSING_TITLE_CODE and message MISSING_TITLE_MESSAGE
     */
    @Override
    public void validateTitle(String title) throws ValidationException {
        if (title == null || title.isEmpty()) {
            throw new ValidationException(MISSING_TITLE_CODE, MISSING_TITLE_MESSAGE);
        }
    }

    /**
     * Validate the searching released year should not exceed the current year
     * @param releasedYear the searching released year
     * @throws ValidationException ValidationException with code INVALID_YEAR_CODE and message INVALID_YEAR_MESSAGE
     */
    @Override
    public void validateReleasedYear(String releasedYear) throws ValidationException {
        if (releasedYear != null && Integer.parseInt(releasedYear) > Year.now().getValue()) {
            throw new ValidationException(INVALID_YEAR_CODE, INVALID_YEAR_MESSAGE);
        }
    }

    /**
     * Validate the searching result's limit to make sure it's either 10 or 20 or 30
     * @param limit the searching result's limit
     * @throws ValidationException ValidationException with code INVALID_LIMIT_CODE and message INVALID_LIMIT_MESSAGE
     */
    @Override
    public void validateLimit(Integer limit) throws ValidationException {
        if (limit != 10 && limit != 20 && limit != 30) {
            throw new ValidationException(INVALID_LIMIT_CODE, INVALID_LIMIT_MESSAGE);
        }
    }

    /**
     * Validate the searching result's page not to be < 0
     * @param page the page number for pagination
     * @throws ValidationException ValidationException with code INVALID_PAGE_CODE and message INVALID_PAGE_MESSAGE
     */
    @Override
    public void validatePage(Integer page) throws ValidationException {
        if (page < 0) {
            throw new ValidationException(INVALID_PAGE_CODE, INVALID_PAGE_MESSAGE);
        }
    }

    /**
     * Validate the searching field orderBy and make sure it can either be rating or title or releaseTime
     * @param orderBy the field that orders the searching results
     * @throws ValidationException ValidationException with code INVALID_ORDER_BY_CODE and message INVALID_ORDER_BY_MESSAGE
     */
    @Override
    public void validateOrderBy(String orderBy) throws ValidationException {
        if (!orderBy.equals("rating") && !orderBy.equals("title") && !orderBy.equals("releaseTime")) {
            throw new ValidationException(INVALID_ORDER_BY_CODE, INVALID_ORDER_BY_MESSAGE);
        }
    }

    /**
     * Validate the searching field direction and make sure it can either be asc or desc.
     * @param direction The direction that orders the results
     * @throws ValidationException ValidationException with code INVALID_DIRECTION_CODE and message INVALID_DIRECTION_MESSAGE
     */
    @Override
    public void validateDirection(String direction) throws ValidationException {
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new ValidationException(INVALID_DIRECTION_CODE, INVALID_DIRECTION_MESSAGE);
        }
    }
}
