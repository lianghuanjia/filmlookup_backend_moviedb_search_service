package com.example.movie_service.exception;

//import com.example.movie_service.config.ErrorResponseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.movie_service.config.ResponseConstants;
import com.example.movie_service.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseConstants responseConstants;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Constructor to inject ResponseConfig dependency
     * @param responseConstants the response configuration. It is a Map of Map that maps the custom response and message
     *                       in the application.yaml
     */
    @Autowired
    public GlobalExceptionHandler(ResponseConstants responseConstants){
        this.responseConstants = responseConstants;
    }

    /**
     * Handles ValidationException exceptions
     * @param exception the ValidationException
     * @return a ResponseEntity with a custom response and BAD_REQUEST status.
     *           (The custom response has response code, message, and data retrieved from service. The data is null in
     *           this case since the validationException will be thrown before going into the service)
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomResponse<Object>> handleRequestParamValidationException(ValidationException exception){
        ResponseConstants.ResponseCodeAndMessage responseCodeAndMessage = responseConstants.getError().get(exception.getValidation_error_type());
        CustomResponse<Object> response = new CustomResponse<>(responseCodeAndMessage.getCode(), responseCodeAndMessage.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException exception){
        ResponseConstants.ResponseCodeAndMessage responseCodeAndMessage = responseConstants.getError().get(exception.getNotFoundResource());
        CustomResponse<Object> response = new CustomResponse<>(responseCodeAndMessage.getCode(), responseCodeAndMessage.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles MissingServletRequestParameterException exceptions. This Handler will catch this exception when there is
     * missing required parameters in the request.
     * @param exception the MissingServletRequestParameterException exception
     * @return a ResponseEntity with a custom response and BAD_REQUEST status.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception){
        String parameterName = exception.getParameterName();
        ResponseConstants.ResponseCodeAndMessage responseCodeAndMessage = responseConstants.getError().get("missing_"+parameterName);
        CustomResponse<Object> response = new CustomResponse<>(responseCodeAndMessage.getCode(), responseCodeAndMessage.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions
     * @param exception the general Exception
     * @return a ResponseEntity with a custom response code, message, and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleGeneralException(Exception exception){
        logger.error("Exception occurred: ", exception);
        return new ResponseEntity<>(new CustomResponse<>(500, exception.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
