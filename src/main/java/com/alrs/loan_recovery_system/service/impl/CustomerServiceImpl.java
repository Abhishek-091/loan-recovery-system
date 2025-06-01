package com.alrs.loan_recovery_system.service.impl;

import com.alrs.loan_recovery_system.entity.Account;
import com.alrs.loan_recovery_system.entity.Customer;
import com.alrs.loan_recovery_system.models.UserModel;
import com.alrs.loan_recovery_system.repository.CustomerRepository;
import com.alrs.loan_recovery_system.service.AccountService;
import com.alrs.loan_recovery_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    @Override
    @Transactional
    public void signUp(UserModel userModel) {
        // Check if username or email already exists
        if (customerRepository.existsByUsername(userModel.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (customerRepository.existsByEmail(userModel.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Save the user model to the repository
        Customer customer = Customer.builder()
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .password(userModel.getPassword()) // In a real application, ensure to hash the password
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .phoneNumber(userModel.getPhoneNumber())
                .isActive(true)
                .emailVerified(false) // Assuming email verification is not handled in this example
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        // Create an account for the customer
        Account account = accountService.createAccount(savedCustomer);
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
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!password.equals(customer.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return Map.of(
                "message", "Sign-in successful",
                "username", customer.getUsername(),
                "email", customer.getEmail()
        );
        // Logic for successful sign-in (e.g., generating a session token) can be added here
    }
}
