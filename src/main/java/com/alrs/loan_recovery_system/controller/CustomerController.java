package com.alrs.loan_recovery_system.controller;

import com.alrs.loan_recovery_system.models.UserModel;
import com.alrs.loan_recovery_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserModel userModel) {
        customerService.signUp(userModel);
        return ResponseEntity.ok("User signed up successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> signIn(@RequestParam String username, @RequestParam String password) {
        Map<String, String> response = customerService.signIn(username, password);
        return ResponseEntity.ok(response);
    }
}
