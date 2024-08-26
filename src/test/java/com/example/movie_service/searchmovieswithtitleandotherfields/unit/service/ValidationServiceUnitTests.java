package com.example.movie_service.searchmovieswithtitleandotherfields.unit.service;

import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.service.ValidationServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static com.example.movie_service.constant.MovieConstant.INVALID_DIRECTION_CODE;
import static com.example.movie_service.constant.MovieConstant.INVALID_DIRECTION_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.INVALID_LIMIT_CODE;
import static com.example.movie_service.constant.MovieConstant.INVALID_LIMIT_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.INVALID_ORDER_BY_CODE;
import static com.example.movie_service.constant.MovieConstant.INVALID_ORDER_BY_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.INVALID_PAGE_CODE;
import static com.example.movie_service.constant.MovieConstant.INVALID_PAGE_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.INVALID_YEAR_CODE;
import static com.example.movie_service.constant.MovieConstant.INVALID_YEAR_MESSAGE;
import static com.example.movie_service.constant.MovieConstant.MISSING_TITLE_CODE;
import static com.example.movie_service.constant.MovieConstant.MISSING_TITLE_MESSAGE;
import static com.example.movie_service.constants.TestConstant.ASC;
import static com.example.movie_service.constants.TestConstant.DESC;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ValidationServiceUnitTests {

    private final ValidationServiceImpl validationService = new ValidationServiceImpl();

    @Test
    void validateTitle_ShouldThrowException_WhenTitleIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateTitle(null));
        assertEquals(MISSING_TITLE_CODE, exception.getErrorCode());
        assertEquals(MISSING_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateTitle_ShouldThrowException_WhenTitleIsEmpty() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateTitle(""));
        assertEquals(MISSING_TITLE_CODE, exception.getErrorCode());
        assertEquals(MISSING_TITLE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateTitle_ShouldNotThrowException_WhenTitleIsValid() {
        assertDoesNotThrow(() -> validationService.validateTitle("Inception"));
    }

    @Test
    void validateReleasedYear_ShouldNotThrowException_WhenYearIsNull() {
        assertDoesNotThrow(() -> validationService.validateReleasedYear(null));
    }

    @Test
    void validateReleasedYear_ShouldThrowException_WhenYearIsInTheFuture() {
        String futureYear = String.valueOf(Year.now().getValue() + 1);
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateReleasedYear(futureYear));
        assertEquals(INVALID_YEAR_CODE, exception.getErrorCode());
        assertEquals(INVALID_YEAR_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateReleasedYear_ShouldNotThrowException_WhenYearIsValid() {
        assertDoesNotThrow(() -> validationService.validateReleasedYear(String.valueOf(Year.now().getValue())));
    }

    @Test
    void validateLimit_ShouldThrowException_WhenLimitIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateLimit(5));
        assertEquals(INVALID_LIMIT_CODE, exception.getErrorCode());
        assertEquals(INVALID_LIMIT_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateLimit_ShouldNotThrowException_WhenLimitIsValid() {
        // Parameterized test, also put assert in the test
        assertDoesNotThrow(() -> validationService.validateLimit(10));
        assertDoesNotThrow(() -> validationService.validateLimit(20));
        assertDoesNotThrow(() -> validationService.validateLimit(30));
    }

    @Test
    void validatePage_ShouldThrowException_WhenPageIsNegative() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validatePage(-1));
        assertEquals(INVALID_PAGE_CODE, exception.getErrorCode());
        assertEquals(INVALID_PAGE_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validatePage_ShouldNotThrowException_WhenPageIsValid() {
        assertDoesNotThrow(() -> validationService.validatePage(0));
        assertDoesNotThrow(() -> validationService.validatePage(1));
    }

    @Test
    void validateOrderBy_ShouldThrowException_WhenOrderByIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateOrderBy("time"));
        assertEquals(INVALID_ORDER_BY_CODE, exception.getErrorCode());
        assertEquals(INVALID_ORDER_BY_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateOrderBy_ShouldNotThrowException_WhenOrderByIsValid() {
        assertDoesNotThrow(() -> validationService.validateOrderBy("rating"));
        assertDoesNotThrow(() -> validationService.validateOrderBy("title"));
        assertDoesNotThrow(() -> validationService.validateOrderBy("releaseTime"));
    }

    @Test
    void validateDirection_ShouldThrowException_WhenDirectionIsInvalid() {
        ValidationException exception = assertThrows(ValidationException.class, () -> validationService.validateDirection("north"));
        assertEquals(INVALID_DIRECTION_CODE, exception.getErrorCode());
        assertEquals(INVALID_DIRECTION_MESSAGE, exception.getErrorMessage());
    }

    @Test
    void validateDirection_ShouldNotThrowException_WhenDirectionIsValid() {
        assertDoesNotThrow(() -> validationService.validateDirection(ASC));
        assertDoesNotThrow(() -> validationService.validateDirection(DESC));
    }
}
