package com.kazama.SpringOAuth2.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.exception.InvalidTokenException;
import com.kazama.SpringOAuth2.exception.RequestLimitExceededException;

@ControllerAdvice
public class GlobalExpectionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleAccountStatusException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username or password is incorrect");
    }

    @ExceptionHandler(RequestLimitExceededException.class)
    public ResponseEntity<?> handleRequestLimitExceeded(RequestLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(498).body(ex.getMessage());
    }

}
