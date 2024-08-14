package com.example.movie_service.service;

import com.example.movie_service.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static com.example.movie_service.constant.MovieConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationServiceTest {

    private final ValidationServiceImpl validationService = new ValidationServiceImpl();

    @Test
    void validateTitle_ShouldThrowException_WhenTitleIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateTitle(null));
        assertEquals(exception.getErrorCode(), MISSING_TITLE_CODE);
        assertEquals(exception.getErrorMessage(), MISSING_TITLE_MESSAGE);
    }

    @Test
    void validateTitle_ShouldThrowException_WhenTitleIsEmpty() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateTitle(""));
        assertEquals(exception.getErrorCode(), MISSING_TITLE_CODE);
        assertEquals(exception.getErrorMessage(), MISSING_TITLE_MESSAGE);
    }

    @Test
    void validateTitle_ShouldNotThrowException_WhenTitleIsValid() {
        validationService.validateTitle("Inception");
    }

    @Test
    void validateReleasedYear_ShouldThrowException_WhenYearIsInTheFuture() {
        String futureYear = String.valueOf(Year.now().getValue() + 1);
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateReleasedYear(futureYear));
        assertEquals(exception.getErrorCode(), INVALID_YEAR_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_YEAR_MESSAGE);
    }

    @Test
    void validateReleasedYear_ShouldNotThrowException_WhenYearIsValid() {
        validationService.validateReleasedYear(String.valueOf(Year.now().getValue()));
    }

    @Test
    void validateLimit_ShouldThrowException_WhenLimitIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateLimit(5));
        assertEquals(exception.getErrorCode(), INVALID_LIMIT_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_LIMIT_MESSAGE);
    }

    @Test
    void validateLimit_ShouldNotThrowException_WhenLimitIsValid() {
        validationService.validateLimit(10);
        validationService.validateLimit(20);
        validationService.validateLimit(30);
    }

    @Test
    void validatePage_ShouldThrowException_WhenPageIsNegative() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validatePage(-1));
        assertEquals(exception.getErrorCode(), INVALID_PAGE_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_PAGE_MESSAGE);
    }

    @Test
    void validatePage_ShouldNotThrowException_WhenPageIsValid() {
        validationService.validatePage(0);
        validationService.validatePage(1);
    }

    @Test
    void validateOrderBy_ShouldThrowException_WhenOrderByIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateOrderBy("time"));
        assertEquals(exception.getErrorCode(), INVALID_ORDER_BY_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_ORDER_BY_MESSAGE);
    }

    @Test
    void validateOrderBy_ShouldNotThrowException_WhenOrderByIsValid() {
        validationService.validateOrderBy("rating");
        validationService.validateOrderBy("title");
        validationService.validateOrderBy("releaseTime");
    }

    @Test
    void validateDirection_ShouldThrowException_WhenDirectionIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateDirection("north"));
        assertEquals(exception.getErrorCode(), INVALID_DIRECTION_CODE);
        assertEquals(exception.getErrorMessage(), INVALID_DIRECTION_MESSAGE);
    }

    @Test
    void validateDirection_ShouldNotThrowException_WhenDirectionIsValid() {
        validationService.validateDirection("asc");
        validationService.validateDirection("desc");
    }
}
