package com.kazama.SpringOAuth2.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Cookie> getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge)
            throws UnsupportedEncodingException {
        String encodedValue = URLEncoder.encode(value, "UTF-8");
        Cookie cookie = new Cookie(key, encodedValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(800);
        cookie.setSecure(true);
        cookie.setDomain("3.27.70.254");
        // cookie.setPath("http://localhost:8080");
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
            return null;
        }

    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {

        if (cookie == null || StringUtils.isEmpty(cookie.getValue())) {
            return null;
        }

        try {
            String decodedCookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
            System.out.println("----------------------------------------");
            System.out.println("decodedCookieValue: " + decodedCookieValue);
            System.out.println("----------------------------------------");

            JsonNode jsonNode = objectMapper.readTree(decodedCookieValue);
            ((ObjectNode) jsonNode).remove("grantType");
            SimpleModule module = new SimpleModule();
            module.addDeserializer(OAuth2AuthorizationResponseType.class,
                    new OAuth2AuthorizationResponseTypeDeserializer());
            objectMapper.registerModule(module);
            return objectMapper.treeToValue(jsonNode, cls);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}