package com.nashtech.rookies.java05.AssetManagement.exceptions.handlers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ErrorResponse;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ ResourceNotFoundException.class })
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException exception,
                                                                            WebRequest request) {
        ErrorResponse error = new ErrorResponse( HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
}