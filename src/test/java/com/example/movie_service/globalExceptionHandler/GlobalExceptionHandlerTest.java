package com.example.movie_service.globalExceptionHandler;

import com.example.movie_service.exception.GlobalExceptionHandler;
import com.example.movie_service.exception.ResourceNotFoundException;
import com.example.movie_service.exception.ValidationException;
import com.example.movie_service.response.CustomResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static com.example.movie_service.constant.MovieConstant.INTERNAL_SERVICE_ERROR_CODE;
import static com.example.movie_service.constant.MovieConstant.MISSING_REQUIRED_PARAMETER_CODE;
import static com.example.movie_service.constant.MovieConstant.MISSING_REQUIRED_PARAMETER_MESSAGE_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    private final int errorCode = 10001;
    private final String errorMessage = "Error message";

    @Test
    void testHandleRequestParamValidationException() {
        // Mock the ValidationException
        ValidationException validationException = mock(ValidationException.class);

        // Mock the behavior of getErrorCode() and getErrorMessage() in the exception
        Mockito.when(validationException.getErrorCode()).thenReturn(errorCode);
        Mockito.when(validationException.getErrorMessage()).thenReturn(errorMessage);

        // Call the method
        ResponseEntity<CustomResponse<Object>> responseEntity = globalExceptionHandler.handleRequestParamValidationException(validationException);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorCode, responseEntity.getBody().getCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    void testHandleResourceNotFoundException() {
        // Mock the exception class
        ResourceNotFoundException resourceNotFoundException = mock(ResourceNotFoundException.class);

        // Mock the behavior of the resourceNotFoundException
        Mockito.when(resourceNotFoundException.getErrorCode()).thenReturn(errorCode);
        Mockito.when(resourceNotFoundException.getErrorMessage()).thenReturn(errorMessage);

        // Call the method
        ResponseEntity<CustomResponse<Object>> responseEntity = globalExceptionHandler.handleResourceNotFoundException(resourceNotFoundException);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorCode, responseEntity.getBody().getCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    void testHandleMissingServletRequestParameterException() {
        String missingParameter = "title";
        MissingServletRequestParameterException missingServletRequestParameterException = mock(MissingServletRequestParameterException.class);

        Mockito.when(missingServletRequestParameterException.getParameterName()).thenReturn(missingParameter);

        ResponseEntity<CustomResponse<Object>> responseEntity = globalExceptionHandler.handleMissingServletRequestParameterException(missingServletRequestParameterException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MISSING_REQUIRED_PARAMETER_CODE, responseEntity.getBody().getCode());
        assertEquals(MISSING_REQUIRED_PARAMETER_MESSAGE_PREFIX+missingParameter, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    void testHandleGeneralException() {
        // Mock the exception class
        Exception exception = mock(Exception.class);

        Mockito.when(exception.getMessage()).thenReturn(errorMessage);

        ResponseEntity<CustomResponse<Object>> responseEntity = globalExceptionHandler.handleGeneralException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(INTERNAL_SERVICE_ERROR_CODE, responseEntity.getBody().getCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }
}
