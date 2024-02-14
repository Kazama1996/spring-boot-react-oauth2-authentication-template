package com.kazama.SpringOAuth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.kazama.SpringOAuth2.security.CustomUserDetailsService;
import com.kazama.SpringOAuth2.security.JWTFilter;
import com.kazama.SpringOAuth2.security.RestAuthenticationEntryPoint;
import com.kazama.SpringOAuth2.security.oauth2.CustomOAuth2UserService;
import com.kazama.SpringOAuth2.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.kazama.SpringOAuth2.security.oauth2.OAuthAuthenticationSuccessHandler;
import com.kazama.SpringOAuth2.security.oauth2.OAuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
        @Autowired
        @Qualifier("delegatedAuthenticationEntryPoint")
        private AuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
                return new HttpCookieOAuth2AuthorizationRequestRepository();
        }

        private JWTFilter jwtFilter;

        private CustomOAuth2UserService customOAuth2UserService;

        private OAuthenticationFailureHandler oAuthenticationFailureHandler;

        private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder());
                provider.setUserDetailsService(customUserDetailsService);
                return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(10);
        }

        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                return http.csrf(csrf -> csrf.disable())
                                .sessionManagement(managemennt -> managemennt
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(

                                                auth -> auth.requestMatchers(
                                                                "/oauth2/**",
                                                                "/api-docs/**", "/swagger-ui/**", "/api/v1/**",
                                                                "/login/**")

                                                                .permitAll()
                                                                .requestMatchers("/",
                                                                                "/error",
                                                                                "/favicon.ico", "/swagger-ui.html",
                                                                                "/v3/api-docs/**")
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .exceptionHandling(
                                                handling -> handling.authenticationEntryPoint(
                                                                new RestAuthenticationEntryPoint()))
                                .authenticationProvider(authenticationProvider())
                                .logout(logout -> logout.logoutUrl("/api/v1/auth/public/logout").permitAll()
                                                .deleteCookies("jwt")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        System.out.println("New version logoutSucceeHandler");
                                                        response.setContentType("application/json");
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                        System.out.println("Updated version");
                                                        response.getWriter().write(
                                                                        "Logout success! Hope to see you again~~");

                                                }).clearAuthentication(true))
                                // oauth2/authorize
                                .oauth2Login(login -> login
                                                .authorizationEndpoint(endpoint -> endpoint
                                                                .baseUri("/oauth2/authorize")
                                                                .authorizationRequestRepository(
                                                                                this.cookieAuthorizationRequestRepository()))
                                                /*
                                                 * the default redirectionEndpoint's baseUri is "/login/oauth2/code/*
                                                 * if you want to customize your own baseUri please do as follow:
                                                 * 
                                                 * If your registrId is "google" also your customize baseUri is :
                                                 * "/oauth2/callback/*"
                                                 * 
                                                 * 1. go to google api console , click your application's credentials
                                                 * 2. set redirect_uri : /oauth2/callback/*
                                                 * 3. set your spring security configuration class as follow:
                                                 * 
                                                 * .redirectionEndpoint(endpoint ->
                                                 * endpoint.baseUri("/oauth2/callback/*"))
                                                 * 
                                                 */
                                                .userInfoEndpoint(info -> info.userService(customOAuth2UserService))
                                                .successHandler(oAuthAuthenticationSuccessHandler)
                                                .failureHandler(oAuthenticationFailureHandler))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                                .build();
        }

}
