package com.alrs.loan_recovery_system.service;

import com.alrs.loan_recovery_system.entity.Account;
import com.alrs.loan_recovery_system.entity.Customer;

public interface AccountService {
    Account createAccount(Customer customer);
}
