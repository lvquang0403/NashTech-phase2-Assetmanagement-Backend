package com.nashtech.rookies.java05.AssetManagement.exceptions.handlers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ErrorResponse;
import com.nashtech.rookies.java05.AssetManagement.exceptions.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ ResourceNotFoundException.class })
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException exception,
                                                                            WebRequest request) {
        ErrorResponse error = new ErrorResponse( HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ RepeatDataException.class})
    protected ResponseEntity handleRepeatDataException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse( HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity handleIllegalArgumentException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse( HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
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

    @ExceptionHandler({UnauthorizedException.class})
    protected ResponseEntity handelUnauthorizedException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
    }

    //Handle exception for Validation (@Valid)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        ErrorResponse errors = ErrorResponse.builder().build();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String errorMessage = error.getDefaultMessage();
//            errors.setCode(HttpStatus.BAD_REQUEST.toString());
//            errors.setMessage(errorMessage);
//        });
//        return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
//    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request){
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Argument Not Valid !!",
                errorMap
        ));
    }
}
