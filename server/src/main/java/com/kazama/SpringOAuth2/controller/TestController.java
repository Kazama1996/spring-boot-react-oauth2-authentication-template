package com.kazama.SpringOAuth2.controller;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userss")
public class TestController {

    @GetMapping("/private/currentuser")
    public String PrivateTest() {
        return "private Test";
    }

    @GetMapping("/currentuser")
    public String PublicTest() {
        return "public Test";
    }
}
