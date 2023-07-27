package com.kazama.jwt.service;

import java.time.Instant;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kazama.jwt.Security.JwtService;
import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.dto.response.AuthResponse;
import com.kazama.jwt.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static com.kazama.jwt.Security.Role.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public ResponseEntity<?> createUser(AuthRequest request ,HttpServletResponse response)
    {
        String password = request.getPassword();
        Instant current = Instant.now();
        User user = User.builder().fullName(request.getFullName()).profileName(request.getProfileName()).email(request.getEmail()).password(passwordEncoder.encode(password)).updateAt(current).role(USER).build();
        userRepository.save(user);
        String jwtToken = jwtService.genJwt(user);
        Cookie cookie = new Cookie("jwt", jwtToken);
        response.addCookie(cookie);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.CREATED).token(jwtToken).build();
        return ResponseEntity.ok().body(responseBody);
    }


    public ResponseEntity<?> authenticate(LoginRequest request, HttpServletResponse response){

        User targetUser = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(targetUser.getUserId().toString(),request.getPassword()));
      
        
        String jwtToken = jwtService.genJwt(targetUser);

        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.OK).token(jwtToken).build();
        response.addCookie(cookie);
        return ResponseEntity.ok().body(responseBody);
    }
}
