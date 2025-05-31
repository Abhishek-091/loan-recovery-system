package com.alrs.loan_recovery_system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class Demo {

    // Define your endpoints here

    // Example endpoint
    @GetMapping("/hello")
    public Map<String, String> hello() {
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "Hello, World!");
        return response;
    }

    // Add more endpoints as needed
}
