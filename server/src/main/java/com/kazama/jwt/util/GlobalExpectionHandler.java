package com.kazama.jwt.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import com.kazama.jwt.exception.AppException;
import com.kazama.jwt.exception.AuthenticationError;
import com.kazama.jwt.exception.RequestLimitExceededException;
import com.kazama.jwt.exception.InvalidTokenException;

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
    public RedirectView handleInvalidToken(InvalidTokenException ex) {
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        return new RedirectView("http://127.0.0.1:3000/login?tokenInvalid=true");
    }

}
