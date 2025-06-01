package com.alrs.loan_recovery_system.utils;

import java.security.SecureRandom;

public final class AccountUtils {

    private AccountUtils() {
        // Prevent instantiation
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    /**
     * RECOMMENDED METHOD for banking environments
     * Uses SecureRandom for cryptographically secure random generation
     */
//    public static long generateSecureAccountNumber() {
//        // Generate 10-digit number using SecureRandom
//        return 1000000000L + Math.abs(secureRandom.nextLong()) % 9000000000L;
//    }

    /**
     * RECOMMENDED: Use SecureRandom.nextLong(long bound) - Java 17+
     * This is the cleanest and most secure approach
     */
    public static long generateSecureAccountNumber() {
        // Java 17+ has nextLong(long bound) method
        // For older versions, use nextLong(long origin, long bound)
        return 1000000000L + secureRandom.nextLong(9000000000L);

    }
}
