package com.kazama.jwt.security.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.kazama.jwt.util.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_KEY = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        System.out.println("loadAuthorizationRequest");
        return CookieUtils
                .getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);

    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
            HttpServletResponse response) {
        System.out.println("saveAuthorizationRequest : start");

        if (authorizationRequest == null) {
            System.out.println("saveAuthorizationRequest : authorizationRequest==null");
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_KEY);
            return;
        }
        try {
            CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY,
                    CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
            System.out.println("saveAuthorizationRequest: addCookie OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_KEY);

        System.out.println("saveAuthorizationRequest  :  redirectUriAfterLogin  " + redirectUriAfterLogin);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            try {
                CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_KEY, redirectUriAfterLogin,
                        cookieExpireSeconds);
                System.out.println("saveAuthorizationRequest : addCookie REDIRECT_URI_PARAM_COOKIE_KEY");

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

}
