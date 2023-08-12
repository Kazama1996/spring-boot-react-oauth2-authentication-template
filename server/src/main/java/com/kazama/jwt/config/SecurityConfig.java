package com.kazama.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import com.kazama.jwt.config.security.JWTFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class SecurityConfig {
    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    private AuthenticationEntryPoint authenticationEntryPoint;

    private AuthenticationProvider authenticationProvider;

    private JWTFilter jwtFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http.csrf(csrf -> csrf.disable());
        // http.authorizeHttpRequests(
        // auth ->
        // auth.requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated());
        // http.httpBasic(withDefaults());
        // http.httpBasic(basic ->
        // basic.authenticationEntryPoint(authenticationEntryPoint));
        // http.authenticationProvider(authenticationProvider);
        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // return http.build();

        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/v1/auth/**").permitAll().requestMatchers("/hello")
                                .permitAll()
                                .anyRequest().authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
