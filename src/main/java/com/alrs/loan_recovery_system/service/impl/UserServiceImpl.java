package com.alrs.loan_recovery_system.service.impl;

import com.alrs.loan_recovery_system.entity.User;
import com.alrs.loan_recovery_system.models.UserModel;
import com.alrs.loan_recovery_system.repository.UserRepository;
import com.alrs.loan_recovery_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void signUp(UserModel userModel) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userModel.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userModel.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Save the user model to the repository
        User user = User.builder()
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .password(userModel.getPassword()) // In a real application, ensure to hash the password
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .phoneNumber(userModel.getPhoneNumber())
                .isActive(true)
                .emailVerified(false) // Assuming email verification is not handled in this example
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return false;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return false;
    }

    @Override
    public Map<String, String> signIn(String username, String password) {
        // Implement sign-in logic here, such as checking credentials and returning a token or user details
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return Map.of(
                "message", "Sign-in successful",
                "username", user.getUsername(),
                "email", user.getEmail()
        );
        // Logic for successful sign-in (e.g., generating a session token) can be added here
    }
}
