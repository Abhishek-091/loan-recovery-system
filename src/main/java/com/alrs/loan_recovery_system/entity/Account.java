package com.alrs.loan_recovery_system.entity;

import com.alrs.loan_recovery_system.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_account")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Account extends BaseEntity {

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @NotBlank(message = "Account number is required")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Account number must contain only digits")
    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @NotBlank(message = "upi_id is required")
    @Column(name = "upi_id", unique = true, nullable = false, length = 20)
    private String upiId;

    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Size(max = 11, message = "IFSC code must not exceed 11 characters")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code format is invalid")
    @Column(name = "ifsc_code", length = 11)
    private String ifscCode;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Balance format is invalid")
    @Column(name = "balance", nullable = false, precision = 15, scale = 2,
            columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
