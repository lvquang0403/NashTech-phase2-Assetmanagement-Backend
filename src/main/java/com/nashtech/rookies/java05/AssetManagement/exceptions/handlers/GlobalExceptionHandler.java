package com.nashtech.rookies.java05.AssetManagement.exceptions.handlers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ErrorResponse;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ForbiddenException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException exception,
                                                                            WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RepeatDataException.class})
    protected ResponseEntity handleRepeatDataException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity handleIllegalArgumentException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NullPointerException.class})
    protected ResponseEntity handleNullPointerException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity handelBadRequestException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ForbiddenException.class})
    protected ResponseEntity handelForbiddenException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.FORBIDDEN);
    }
}
