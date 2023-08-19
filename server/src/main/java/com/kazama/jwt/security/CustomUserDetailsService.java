package com.kazama.jwt.security;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.model.User;
import com.kazama.jwt.security.oauth2.UserPrincipal;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUserEmail");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User: " +user.toString());
        return UserPrincipal.create(user);
    }

    public UserPrincipal loadByUserId(String userIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserPrincipal.create(user);

    }

    // @Bean
    // protected UserDetailsService userDetailsService() {
    // return email -> userRepository.findByEmail(email)
    //
    // }
}
