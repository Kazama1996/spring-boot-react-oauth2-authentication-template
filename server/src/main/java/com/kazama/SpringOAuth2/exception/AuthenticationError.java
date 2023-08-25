package com.kazama.SpringOAuth2.exception;

public class AuthenticationError extends RuntimeException {
    public AuthenticationError(String message) {
        super(message);
    }
}
