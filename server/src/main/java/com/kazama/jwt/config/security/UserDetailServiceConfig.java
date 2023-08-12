package com.kazama.jwt.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kazama.jwt.dao.UserRepository;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class UserDetailServiceConfig {

    private UserRepository userRepository;

    @Bean
    protected UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
