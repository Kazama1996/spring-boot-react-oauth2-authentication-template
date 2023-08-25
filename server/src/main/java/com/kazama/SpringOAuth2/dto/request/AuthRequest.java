package com.kazama.SpringOAuth2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    private String fullName;
    private String profileName;
    private String email;
    private String password;
}
