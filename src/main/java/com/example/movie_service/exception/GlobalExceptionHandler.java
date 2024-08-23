package com.example.movie_service.exception;

import lombok.extern.slf4j.Slf4j;
import com.example.movie_service.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.movie_service.constant.MovieConstant.*;

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

    @ExceptionHandler(NoCustomIdGeneratorAnnotationFoundInEntityException.class)
    public ResponseEntity<CustomResponse<Object>> handleNoCustomIdGeneratorAnnotationFoundInEntity(NoCustomIdGeneratorAnnotationFoundInEntityException exception) {
        CustomResponse<Object> response = new CustomResponse<>(exception.getErrorCode(), exception.getErrorMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error("handleResourceNotFoundException: ", exception);
        CustomResponse<Object> response = new CustomResponse<>(exception.getErrorCode(), exception.getErrorMessage(), null);
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
