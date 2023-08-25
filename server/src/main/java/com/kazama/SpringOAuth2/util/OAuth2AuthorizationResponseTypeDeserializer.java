package com.kazama.SpringOAuth2.util;

import java.io.IOException;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class OAuth2AuthorizationResponseTypeDeserializer extends StdDeserializer<OAuth2AuthorizationResponseType> {

    public OAuth2AuthorizationResponseTypeDeserializer() {
        super(OAuth2AuthorizationRequest.class);
    }

    @Override
    public OAuth2AuthorizationResponseType deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        String responseTypeValue = node.get("value").asText();

        return new OAuth2AuthorizationResponseType(responseTypeValue);

    }

}
