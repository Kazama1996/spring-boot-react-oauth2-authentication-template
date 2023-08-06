package com.kazama.jwt.exception;

import lombok.Builder;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
