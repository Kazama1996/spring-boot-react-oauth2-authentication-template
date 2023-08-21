package com.kazama.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kazama.jwt.security.JWTFilter;
import com.kazama.jwt.security.oauth2.CustomOAuth2UserService;
import com.kazama.jwt.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.kazama.jwt.security.oauth2.OAuthAuthenticationSuccessHandler;
import com.kazama.jwt.security.oauth2.OAuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
        @Autowired
        @Qualifier("delegatedAuthenticationEntryPoint")
        private AuthenticationEntryPoint authenticationEntryPoint;

        private AuthenticationProvider authenticationProvider;

        private JWTFilter jwtFilter;

        private CustomOAuth2UserService customOAuth2UserService;

        private OAuthenticationFailureHandler oAuthenticationFailureHandler;

        private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

        @Bean
        public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
                return new HttpCookieOAuth2AuthorizationRequestRepository();
        }

        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                return http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(
                                                auth -> auth.requestMatchers("/api/v1/auth/**", "/favicon.ico")
                                                                .permitAll()
                                                                .requestMatchers("/oauth2/**")
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .httpBasic(basic -> basic.disable())
                                .exceptionHandling(
                                                handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                                .authenticationProvider(authenticationProvider)
                                // oauth2/authorize
                                .oauth2Login(login -> login
                                                .authorizationEndpoint(endpoint -> endpoint
                                                                .baseUri("/oauth2/authorize")
                                                                .authorizationRequestRepository(
                                                                                cookieOAuth2AuthorizationRequestRepository()))
                                                .redirectionEndpoint(endpoint -> endpoint
                                                                .baseUri("/oauth2/callback/*"))
                                                .userInfoEndpoint(info -> info.userService(customOAuth2UserService))
                                                .successHandler(oAuthAuthenticationSuccessHandler)
                                                .failureHandler(oAuthenticationFailureHandler))

                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
