package com.example.movie_service.exception;

import lombok.extern.slf4j.Slf4j;
import com.example.movie_service.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.example.movie_service.constant.MovieConstant.*;

/**
 * This class handles all the Exceptions
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ValidationException exceptions
     *
     * @param exception the ValidationException
     * @return a ResponseEntity with a custom response and BAD_REQUEST status.
     * (The custom response has response code, message, and data retrieved from service. The data is null in
     * this case since the validationException will be thrown before going into the service)
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomResponse<Object>> handleRequestParamValidationException(ValidationException exception) {
        log.error("handleRequestParamValidationException: ", exception);
        CustomResponse<Object> response = new CustomResponse<>(exception.getErrorCode(), exception.getErrorMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles NoCustomIdGeneratorAnnotationFoundInEntityException
     * @param exception An Exception thrown when an Entity is meant to use @CustomIdGeneratorAnnotation but this annotation is not found in the Entity.
     * @return ResponseEntity with a CustomResponse and INTERNAL_SERVER_ERROR HttpStatus
     */
    @ExceptionHandler(NoCustomIdGeneratorAnnotationFoundInEntityException.class)
    public ResponseEntity<CustomResponse<Object>> handleNoCustomIdGeneratorAnnotationFoundInEntityException(NoCustomIdGeneratorAnnotationFoundInEntityException exception) {
        log.error("handleNoCustomIdGeneratorAnnotationFoundInEntityException: ", exception);
        CustomResponse<Object> response = new CustomResponse<>(exception.getErrorCode(), exception.getErrorMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles ResourceNotFoundException
     * @param exception An Exception thrown when a resource is not found
     * @return ResponseEntity with a CustomResponse and NOT_FOUND HttpStatus
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error("handleResourceNotFoundException: ", exception);
        CustomResponse<Object> response = new CustomResponse<>(exception.getErrorCode(), exception.getErrorMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles NoResourceFoundException.
     * @param exception NoResourceFoundException. This exception is thrown by controllers when an endpoint is not found.
     *                  There could be other situation this exception is thrown.
     * @return ResponseEntity with a CustomResponse and NOT_FOUND HttpStatus
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleNoResourceFoundException(NoResourceFoundException exception){
        log.error("handleNoResourceFoundException: ", exception);
        CustomResponse<Object> response = new CustomResponse<>(HttpStatus.NOT_FOUND.value(), exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles MissingServletRequestParameterException exceptions. This Handler will catch this exception when there is
     * missing required parameters in the request.
     *
     * @param exception the MissingServletRequestParameterException exception
     * @return a ResponseEntity with a custom response and BAD_REQUEST status.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("handleMissingServletRequestParameterException: ", exception);
        String parameterName = exception.getParameterName();
        CustomResponse<Object> response = new CustomResponse<>(MISSING_REQUIRED_PARAMETER_CODE,
                MISSING_REQUIRED_PARAMETER_MESSAGE_PREFIX + parameterName, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions
     *
     * @param exception the general Exception
     * @return a ResponseEntity with a custom response code, message, and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleGeneralException(Exception exception) {
        log.error("handleGeneralException: ", exception);
        return new ResponseEntity<>(new CustomResponse<>(INTERNAL_SERVICE_ERROR_CODE, exception.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
