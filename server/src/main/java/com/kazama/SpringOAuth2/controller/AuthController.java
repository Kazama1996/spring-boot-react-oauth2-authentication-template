package com.kazama.SpringOAuth2.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
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
import com.kazama.SpringOAuth2.dto.response.AuthResponse;
import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")

public class AuthController {

    @Autowired
    private final AuthService authService;

    @Operation(summary = "Create a new user", description = "provide your email, fullName, profileName and password in a JSON format inform the DB to create a new user. ")
    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created user success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "create user fail with an invalid request e.g. empty profileName or email", content = {
                    @Content(mediaType = "text/plain;charset=UTF-8", schema = @Schema(implementation = String.class))
            })

    })
    public ResponseEntity<?> register(@RequestBody AuthRequest reqBody, HttpServletResponse response)
            throws UnsupportedEncodingException {

        return authService.createUser(reqBody, response);
    }

    @Operation(summary = "login", description = "provide your email and password to login the system")
    @PostMapping("/authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login success", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class)))
            })
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest reqBody, HttpServletResponse response)
            throws UnsupportedEncodingException {
        return authService.authenticate(reqBody, response);

    }

    @Operation(summary = "Get a password reset link", description = "user input their email then get a password reset link in their inbox.")
    @PostMapping("/forgotPassowrd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "send password reset link success", content = {
                    @Content(mediaType = "text/plain;charset=UTF-8", examples = @ExampleObject(name = "", value = "Send password token to <email which user provided>"))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid email (not present in data base)", content = {
                    @Content(mediaType = "text/plain:charset=UTF-8", examples = @ExampleObject(name = "", value = "Could not found this email : <email which user provided>"))
            })
    })
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {

        return authService.sendPasswordResetEmail(reqBody);
    }

    @Operation(summary = "Redirect to a password reset page", description = "Redirect to the password reset page after user click the password reset linke in their inbox")
    @GetMapping("/passwordReset/{token}")
    @ApiResponses(value = @ApiResponse(responseCode = "303", description = "Password reset link is valid, Redirect success."))
    public RedirectView redirectToUpdatePasswordPage(@PathVariable String token) {
        return authService.redirectUpdatePasswordPage(token);
    }

    @Operation(summary = "Update password", description = "Update password via password which user provided")
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest reqBody) {
        return authService.updatePassword(reqBody);
    }

}
