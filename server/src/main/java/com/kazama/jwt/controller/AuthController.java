package com.kazama.jwt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @GetMapping("/redirect/{token}")
    public RedirectView redirect(@PathVariable String token) {
        return userService.verifyPasswordResetToken(token);
    }

    @PatchMapping("/resetPassword/{token}")
    public RedirectView updatePassword(@RequestBody String newPassword, @PathVariable String passwordResetToken) {
        return userService.updatePassword(newPassword, passwordResetToken);
    }
}
