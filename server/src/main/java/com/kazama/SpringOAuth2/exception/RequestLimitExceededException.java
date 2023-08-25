package com.kazama.SpringOAuth2.exception;

public class RequestLimitExceededException extends RuntimeException {
    public RequestLimitExceededException(String message) {
        super(message);
    }

}
