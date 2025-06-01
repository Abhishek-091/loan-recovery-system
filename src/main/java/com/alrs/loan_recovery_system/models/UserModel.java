package com.alrs.loan_recovery_system.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserModel {
    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}


//@Schema(description = "User request object")
//public class UserRequest {
//
//    @Schema(description = "User's full name", example = "John Doe", required = true)
//    @NotBlank(message = "Name is required")
//    private String name;
//
//    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
//    @Email(message = "Invalid email format")
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    @Schema(description = "User's password", example = "SecurePass123!", required = true)
//    @Pattern(
//            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])\\w{8,}$",
//            message = "Password must contain at least 8 characters with one digit, one uppercase, one lowercase, and one special character"
//    )
//    private String password;
//
//    @Schema(description = "User's age", example = "25", minimum = "18", maximum = "120")
//    @Min(value = 18, message = "Age must be at least 18")
//    @Max(value = 120, message = "Age must be less than 120")
//    private Integer age;
//
//    @Schema(description = "User's role", example = "USER")
//    @Enumerated(EnumType.STRING)
//    private UserRole role;
//
//    // Constructors, getters, setters
//}

//@Schema(description = "User response object")
//public class UserResponse {
//
//    @Schema(description = "User ID", example = "1")
//    private Long id;
//
//    @Schema(description = "User's full name", example = "John Doe")
//    private String name;
//
//    @Schema(description = "User's email address", example = "john.doe@example.com")
//    private String email;
//
//    @Schema(description = "User's age", example = "25")
//    private Integer age;
//
//    @Schema(description = "User's role", example = "USER")
//    private UserRole role;
//
//    @Schema(description = "Account creation timestamp", example = "2024-01-15T10:30:00")
//    private LocalDateTime createdAt;
//
//    @Schema(description = "Last modification timestamp", example = "2024-01-15T10:30:00")
//    private LocalDateTime updatedAt;
//
//    // Constructors, getters, setters
//}


//@Schema(description = "Error response object")
//public class ErrorResponse {
//
//    @Schema(description = "Error message", example = "User not found")
//    private String message;
//
//    @Schema(description = "HTTP status code", example = "404")
//    private int status;
//
//    @Schema(description = "Error timestamp", example = "2024-01-15T10:30:00")
//    private LocalDateTime timestamp;
//
//    @Schema(description = "Request path", example = "/api/users/999")
//    private String path;
//
//    // Constructors, getters, setters
//}