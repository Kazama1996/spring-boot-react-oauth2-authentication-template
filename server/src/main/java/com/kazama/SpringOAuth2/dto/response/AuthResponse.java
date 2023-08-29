package com.kazama.SpringOAuth2.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AuthResponse {
    private final String token;
}
