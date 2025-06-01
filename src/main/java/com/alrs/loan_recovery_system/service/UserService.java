package com.alrs.loan_recovery_system.service;

import com.alrs.loan_recovery_system.models.UserModel;

import java.util.Map;

public interface UserService {
    void signUp(UserModel userModel);
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
    Map<String, String> signIn(String username, String password);
}
