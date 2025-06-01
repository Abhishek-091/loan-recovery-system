package com.alrs.loan_recovery_system.service.impl;

import com.alrs.loan_recovery_system.entity.Account;
import com.alrs.loan_recovery_system.entity.Customer;
import com.alrs.loan_recovery_system.repository.AccountRepository;
import com.alrs.loan_recovery_system.service.AccountService;
import com.alrs.loan_recovery_system.utils.AccountUtils;
import com.alrs.loan_recovery_system.utils.IFSCCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        String accountNumber = Long.toString(AccountUtils.generateSecureAccountNumber());
        String ifscCode = IFSCCodeGenerator.generateRandomIFSC();
        String bankName = IFSCCodeGenerator.extractBankCode(ifscCode);
        //Create a new account for the customer : Default account
        Account account = Account.builder()
                .customer(customer)
                .accountNumber(accountNumber)
                .upiId(String.format("%s@%s", accountNumber, bankName.toLowerCase()))
                .ifscCode(ifscCode)
                .bankName(bankName)
                .balance(BigDecimal.valueOf(1000.00))
                .isPrimary(true)
                .isActive(false)
                .build();

        return accountRepository.save(account);
    }
}
