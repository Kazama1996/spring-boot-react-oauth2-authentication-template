package com.kazama.SpringOAuth2.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AuthResponse {
    private final String token;
}
