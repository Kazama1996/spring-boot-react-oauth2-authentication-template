// package com.kazama.SpringOAuth2.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// import com.kazama.SpringOAuth2.security.oauth2.CustomOAuth2UserService;
// import
// com.kazama.SpringOAuth2.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
// import
// com.kazama.SpringOAuth2.security.oauth2.OAuthAuthenticationSuccessHandler;
// import com.kazama.SpringOAuth2.security.oauth2.OAuthenticationFailureHandler;

// @Configuration
// @EnableMethodSecurity
// public class OAuth2SecurityConfig {

// private CustomOAuth2UserService customOAuth2UserService;

// private OAuthenticationFailureHandler oAuthenticationFailureHandler;

// private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

// @Bean
// public HttpCookieOAuth2AuthorizationRequestRepository
// cookieOAuth2AuthorizationRequestRepository() {
// return new HttpCookieOAuth2AuthorizationRequestRepository();
// }

// @Bean
// protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception
// {
// return http.oauth2Login(login -> login
// .authorizationEndpoint(endpoint -> endpoint
// .baseUri("/oauth2/authorize")
// .authorizationRequestRepository(
// cookieOAuth2AuthorizationRequestRepository()))
// .redirectionEndpoint(endpoint -> endpoint
// .baseUri("/oauth2/callback/*"))

// .userInfoEndpoint(info -> info.userService(customOAuth2UserService))
// .successHandler(oAuthAuthenticationSuccessHandler)
// .failureHandler(oAuthenticationFailureHandler)).build();
// }

// }
