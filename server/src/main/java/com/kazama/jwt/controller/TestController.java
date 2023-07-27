package com.kazama.jwt.controller;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kazama.jwt.model.User;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    

    @GetMapping
    public String sayHello(){
        UUID reqUserId = (UUID)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reqUserId.toString(); 
    }
}
