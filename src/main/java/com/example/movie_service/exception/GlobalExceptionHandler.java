package com.example.movie_service.exception;

//import com.example.movie_service.config.ErrorResponseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.movie_service.config.ResponseConfig;
import com.example.movie_service.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.awt.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseConfig responseConfig;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @Autowired
    public GlobalExceptionHandler(ResponseConfig responseConfig){
        this.responseConfig = responseConfig;
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomResponse<Object>> handleRequestParamValidationException(ValidationException exception){
        ResponseConfig.ResponseMessage responseMessage = responseConfig.getError().get(exception.getValidation_error_type());
        CustomResponse<Object> response = new CustomResponse<>(responseMessage.getCode(), responseMessage.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception){
        String parameterName = exception.getParameterName();
        ResponseConfig.ResponseMessage responseMessage = responseConfig.getError().get("missing_"+parameterName);
        CustomResponse<Object> response = new CustomResponse<>(responseMessage.getCode(),responseMessage.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleGeneralException(Exception exception){
        logger.error("Exception occurred: ", exception);
        return new ResponseEntity<>(new CustomResponse<>(500, exception.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
