package com.kazama.jwt.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AuthResponse {
    private final HttpStatus status;
    private final String token;
}
