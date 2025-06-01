package com.alrs.loan_recovery_system.entity;

import com.alrs.loan_recovery_system.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_customer")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", unique = true, nullable = false, length = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must contain at least 8 characters with one digit, one uppercase letter, one lowercase letter, and one special character"
    )
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;


//  Relationships
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Account> accounts = new HashSet<>();
//
//    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<LoanRequest> borrowerRequests = new HashSet<>();
//
//    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<LoanRequest> lenderRequests = new HashSet<>();
//
//    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<Loan> borrowedLoans = new HashSet<>();
//
//    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<Loan> lentLoans = new HashSet<>();

}