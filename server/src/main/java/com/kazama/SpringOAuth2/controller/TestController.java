package com.kazama.SpringOAuth2.controller;

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
