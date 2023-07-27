package com.kazama.jwt.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class LoginRequest {
    private final String email;
    private final String password;
}
