package com.kazama.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest reqBody , HttpServletResponse response){

        return userService.createUser(reqBody , response);
    }    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest reqBody, HttpServletResponse response){
    
        return userService.authenticate(reqBody,  response);
    }    
}
