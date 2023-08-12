package com.kazama.jwt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.ForgotPasswordRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.dto.request.UpdatePasswordRequest;
import com.kazama.jwt.exception.AppException;
import com.kazama.jwt.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest reqBody, HttpServletResponse response) {

        return userService.createUser(reqBody, response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest reqBody, HttpServletResponse response) {

        return userService.authenticate(reqBody, response);
    }

    @PostMapping("/forgotPassowrd")
    public ResponseEntity<?> forgotPassworda(@RequestBody ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {

        return userService.sendPasswordResetEmail(reqBody);
    }

    @GetMapping("/passwordReset/{token}")
    public RedirectView redirectToUpdatePasswordPage(@PathVariable String token) {
        return userService.redirectUpdatePasswordPage(token);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest reqBody) {
        return userService.updatePassword(reqBody);
    }

    @GetMapping("/testRequest")
    public String TestRequest() {
        return "Test";
    }
}
