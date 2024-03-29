package com.kazama.SpringOAuth2.security.oauth2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.kazama.SpringOAuth2.config.AppProperties;
import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.util.CookieUtils;
import com.kazama.SpringOAuth2.util.JWT.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jodd.net.HttpStatus;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private JwtService jwtService;

    public static final String REDIRECT_URI_PARAM_COOKIE_KEY = "redirect_uri";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        String decodedUrl = URLDecoder.decode(targetUrl, "UTF-8");

        // // @ Test without this:
        clearAuthenticationAttributes(request, response);
        // addAccessTokenCookie(request, response, authentication);
        String jwt = jwtService.genJwt(authentication);

        CookieUtils.addCookie(response, "jwt", jwt, 100);

        System.out.println("*************************************");
        System.out.println("This is the new version " + decodedUrl);
        System.out.println("*************************************");

        getRedirectStrategy().sendRedirect(request, response, decodedUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookie(request, response);
    }

    // protected void addAccessTokenCookie(HttpServletRequest request,
    // HttpServletResponse response,
    // Authentication authentication) throws UnsupportedEncodingException {
    // httpCookieOAuth2AuthorizationRequestRepository.addAccessTokenCookie(request,
    // response, authentication);
    // }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        try {
            Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_KEY)
                    .map(Cookie::getValue);
            String decodedUri = URLDecoder.decode(redirectUri.get(), "UTF-8");

            if (redirectUri.isPresent() && !isAuthorizedRedirectUri(decodedUri)) {
                throw new AppException(
                        "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
            }
            String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

            return UriComponentsBuilder.fromUriString(targetUrl).queryParam("success", true).build().toUriString();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAuthorizedRedirectUri(String uri) {

        return appProperties.getOAuth2().getAuthorizedRedirectUris().stream().anyMatch(authorizedRedirectUri -> {
            URI authorizedURI = URI.create(uri);
            if (authorizedURI.getHost().equalsIgnoreCase(authorizedURI.getHost())
                    && authorizedURI.getPort() == authorizedURI.getPort()) {
                return true;
            }
            return false;
        });

    }

}
