package com.kazama.SpringOAuth2.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kazama.SpringOAuth2.dao.UserRepository;
import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.security.CurrentUser;
import com.kazama.SpringOAuth2.security.oauth2.UserPrincipal;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@NoArgsConstructor
@Tag(name = "Profile")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/private/currentuser")
    public ResponseEntity<?> getCurrentUserName(@CurrentUser UserPrincipal userPrincipal) {

        User user = userRepository.findById(UUID.fromString(userPrincipal.getName()))
                .orElseThrow(() -> new AppException("User is not found"));

        return ResponseEntity.ok().body(user.getProfileName());

    }
}
