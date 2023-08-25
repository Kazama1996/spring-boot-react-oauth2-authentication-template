package com.kazama.SpringOAuth2.exception.security;

import javax.naming.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }
}
