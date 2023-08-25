package com.kazama.SpringOAuth2.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

import com.kazama.SpringOAuth2.dto.request.AuthRequest;
import com.kazama.SpringOAuth2.dto.request.ForgotPasswordRequest;
import com.kazama.SpringOAuth2.dto.request.LoginRequest;
import com.kazama.SpringOAuth2.dto.request.UpdatePasswordRequest;
import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.service.AuthService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest reqBody, HttpServletResponse response)
            throws UnsupportedEncodingException {

        return authService.createUser(reqBody, response);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> login(@RequestBody LoginRequest reqBody, HttpServletResponse response)
            throws UnsupportedEncodingException {
        return authService.authenticate(reqBody, response);

    }

    @PostMapping("/forgotPassowrd")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {

        return authService.sendPasswordResetEmail(reqBody);
    }

    @GetMapping("/passwordReset/{token}")
    public RedirectView redirectToUpdatePasswordPage(@PathVariable String token) {
        return authService.redirectUpdatePasswordPage(token);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest reqBody) {
        return authService.updatePassword(reqBody);
    }

    @GetMapping("/testRequest")
    public String TestRequest() {
        return "Test";
    }
}
