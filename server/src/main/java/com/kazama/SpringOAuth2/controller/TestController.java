package com.kazama.SpringOAuth2.controller;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/hello")
    public String ShowHello(Model model) {
        model.addAttribute("hello", "nmsl");
        return "hello";
    }
}
