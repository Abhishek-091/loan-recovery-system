package com.alrs.loan_recovery_system.controller;

import com.alrs.loan_recovery_system.models.UserModel;
import com.alrs.loan_recovery_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {
    
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users with pagination support"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> signUp(@RequestBody UserModel userModel) {
        userService.signUp(userModel);
        return ResponseEntity.ok("User signed up successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> signIn(@RequestParam String username, @RequestParam String password) {
        Map<String, String> response = userService.signIn(username, password);
        return ResponseEntity.ok(response);
    }
}
