package com.kazama.SpringOAuth2.security.oauth2;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.util.CookieUtils;
import com.kazama.SpringOAuth2.util.JWT.JwtService;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_KEY = "redirect_uri";
    public static final String JWT_KEY = "jwt";
    private static final int cookieExpireSeconds = 180;

    @Autowired
    private JwtService jwtService;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils
                .getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);

    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
            HttpServletResponse response) {

        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_KEY);
            return;
        }
        try {
            CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY,
                    CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_KEY);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            try {
                CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_KEY, redirectUriAfterLogin,
                        cookieExpireSeconds);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
            HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);

    }

    public void removeAuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_KEY);
    }

    // public void addAccessTokenCookie(HttpServletRequest request,
    // HttpServletResponse response,
    // Authentication authentication) throws UnsupportedEncodingException {

    // // OAuth2AuthenticationToken oauth2Authentication =
    // (OAuth2AuthenticationToken)
    // // authentication;

    // // System.out.println("-----------------------------------OAuth2
    // // Principal-----------------------------");
    // // System.out.println(oauth2Authentication.getPrincipal());
    // // System.out.println("-----------------------------------Normal
    // // Principal-----------------------------");
    // // System.out.println(authentication.getPrincipal());
    // // System.out.println("-----------------------------------Res
    // // -----------------------------");
    // // System.out.println(oauth2Authentication == authentication);

    // String jwt = jwtService.genJwt(authentication);

    // // String jwt = jwtService.genJwt(authentication);

    // CookieUtils.addCookie(response, "jwt", jwt, cookieExpireSeconds);

    // // Cookie cookie = new Cookie("jwt", jwt);
    // // cookie.setPath("/");
    // // cookie.setHttpOnly(true);
    // // cookie.setMaxAge(800);
    // // cookie.setDomain("localhost");
    // // cookie.setSecure(true);
    // // cookie.setAttribute("SameSite", "None");
    // // response.setHeader("Access-Control-Allow-Headers",
    // // "Date, Content-Type, Accept, X-Requested-With, Authorization,
    // // From,X-Auth-Token, Request-Id");
    // // response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
    // // response.setHeader("Access-Control-Allow-Credentials", "true");

    // // response.addCookie(cookie);

    // }

}
