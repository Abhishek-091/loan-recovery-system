package com.alrs.loan_recovery_system.repository;

import com.alrs.loan_recovery_system.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
