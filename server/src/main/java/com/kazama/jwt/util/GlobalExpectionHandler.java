package com.kazama.jwt.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kazama.jwt.exception.AppException;

@ControllerAdvice
public class GlobalExpectionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleException(AppException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    
}
