package com.alrs.loan_recovery_system.utils;

import java.security.SecureRandom;
import java.util.*;

public class IFSCCodeGenerator {

    private IFSCCodeGenerator() {
        // Private constructor to prevent instantiation
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    // Common bank codes used in India (first 4 characters)
    private static final String[] BANK_CODES = {
            "SBIN", "HDFC", "ICIC", "AXIS", "PUNB", "CANB", "UBIN", "BARB",
            "IDIB", "IOBA", "CORP", "ALLA", "ANDB", "BKID", "CNRB", "COSB",
            "DBSS", "FDRL", "INDB", "KARB", "MAHB", "ORBC", "PSIB", "RATN",
            "SBHY", "SCBL", "SRCB", "SVCB", "TMBL", "UCBA", "UTIB", "VIJB"
    };

    // State/Region codes for branch identification
    private static final Map<String, String[]> STATE_BRANCH_CODES = new HashMap<>();

    static {
        STATE_BRANCH_CODES.put("DELHI", new String[]{"DEL", "NDL", "SDL", "EDL", "WDL"});
        STATE_BRANCH_CODES.put("MUMBAI", new String[]{"MUM", "BOM", "AND", "BAN", "KUR"});
        STATE_BRANCH_CODES.put("BANGALORE", new String[]{"BLR", "BNG", "MAL", "JAY", "KOR"});
        STATE_BRANCH_CODES.put("CHENNAI", new String[]{"CHE", "MAD", "TAM", "VEL", "ANN"});
        STATE_BRANCH_CODES.put("KOLKATA", new String[]{"KOL", "CAL", "HOW", "DUM", "BAL"});
        STATE_BRANCH_CODES.put("HYDERABAD", new String[]{"HYD", "SEC", "KUK", "MED", "RAN"});
        STATE_BRANCH_CODES.put("PUNE", new String[]{"PUN", "PCM", "WAK", "KOT", "SHI"});
        STATE_BRANCH_CODES.put("AHMEDABAD", new String[]{"AHM", "AMD", "GAN", "SAR", "BOD"});
    }


    /**
     * Generate a completely random IFSC code
     * Format: [BANK_CODE][0][BRANCH_CODE]
     */
    public static String generateRandomIFSC() {
        String bankCode = BANK_CODES[secureRandom.nextInt(BANK_CODES.length)];
        String branchCode = generateRandomBranchCode();
        return bankCode + "0" + branchCode;
    }

    /**
     * Generate IFSC for a specific bank
     */
    public static String generateIFSCForBank(String bankCode) {
        if (isValidBankCode(bankCode)) {
            throw new IllegalArgumentException("Invalid bank code: " + bankCode);
        }
        String branchCode = generateRandomBranchCode();
        return bankCode.toUpperCase() + "0" + branchCode;
    }

    /**
     * Generate IFSC for specific bank and location
     */
    public static String generateIFSCForBankAndLocation(String bankCode, String location) {
        if (isValidBankCode(bankCode)) {
            throw new IllegalArgumentException("Invalid bank code: " + bankCode);
        }

        String branchCode;
        if (STATE_BRANCH_CODES.containsKey(location.toUpperCase())) {
            String[] locationCodes = STATE_BRANCH_CODES.get(location.toUpperCase());
            String locationCode = locationCodes[secureRandom.nextInt(locationCodes.length)];
            branchCode = locationCode + generateRandomDigits(3);
        } else {
            branchCode = generateRandomBranchCode();
        }

        return bankCode.toUpperCase() + "0" + branchCode;
    }

    /**
     * Generate custom IFSC with specific bank and branch code
     */
    public static String generateCustomIFSC(String bankCode, String branchCode) {
        if (isValidBankCode(bankCode)) {
            throw new IllegalArgumentException("Invalid bank code: " + bankCode);
        }

        if (branchCode.length() != 6) {
            throw new IllegalArgumentException("Branch code must be exactly 6 characters");
        }

        return bankCode.toUpperCase() + "0" + branchCode.toUpperCase();
    }

    /**
     * Generate multiple unique IFSC codes for a bank
     */
    public static Set<String> generateMultipleUniqueIFSCs(String bankCode, int count) {
        Set<String> uniqueIFSCs = new HashSet<>();
        int maxAttempts = count * 10;
        int attempts = 0;

        while (uniqueIFSCs.size() < count && attempts < maxAttempts) {
            String ifsc = generateIFSCForBank(bankCode);
            uniqueIFSCs.add(ifsc);
            attempts++;
        }

//        if (uniqueIFSCs.size() < count) {
//            throw new RuntimeException("Could not generate " + count + " unique IFSC codes");
//        }

        return uniqueIFSCs;
    }

    /**
     * Generate a random 6-character branch code
     * Can contain letters and numbers
     */
    private static String generateRandomBranchCode() {
        StringBuilder branchCode = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 6; i++) {
            branchCode.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }

        return branchCode.toString();
    }

    /**
     * Generate random digits string
     */
    private static String generateRandomDigits(int length) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < length; i++) {
            digits.append(secureRandom.nextInt(10));
        }
        return digits.toString();
    }

    /**
     * Validate if bank code is recognized
     */
    private static boolean isValidBankCode(String bankCode) {
        if (bankCode == null || bankCode.length() != 4) {
            return true;
        }

        return !Arrays.asList(BANK_CODES).contains(bankCode.toUpperCase());
    }

    /**
     * Validate IFSC format according to RBI standards
     * Format: [4 letters][1 zero][6 alphanumeric]
     */
    public static boolean isValidIFSCFormat(String ifsc) {
        if (ifsc == null || ifsc.length() != 11) {
            return true;
        }

        // First 4 characters should be letters
        String bankPart = ifsc.substring(0, 4);
        if (!bankPart.matches("[A-Z]{4}")) {
            return true;
        }

        // 5th character should be 0
        if (ifsc.charAt(4) != '0') {
            return true;
        }

        // Last 6 characters should be alphanumeric
        String branchPart = ifsc.substring(5);

        return !branchPart.matches("[A-Z0-9]{6}");
    }

    /**
     * Extract bank code from IFSC
     */
    public static String extractBankCode(String ifsc) {
        if (isValidIFSCFormat(ifsc)) {
            throw new IllegalArgumentException("Invalid IFSC format");
        }
        return ifsc.substring(0, 4);
    }

    /**
     * Extract branch code from IFSC
     */
    public static String extractBranchCode(String ifsc) {
        if (isValidIFSCFormat(ifsc)) {
            throw new IllegalArgumentException("Invalid IFSC format");
        }
        return ifsc.substring(5);
    }

    /**
     * Get bank name from bank code (simplified mapping)
     */
    public static String getBankName(String bankCode) {
        Map<String, String> bankNames = new HashMap<>();
        bankNames.put("SBIN", "State Bank of India");
        bankNames.put("HDFC", "HDFC Bank");
        bankNames.put("ICIC", "ICICI Bank");
        bankNames.put("AXIS", "Axis Bank");
        bankNames.put("PUNB", "Punjab National Bank");
        bankNames.put("CANB", "Canara Bank");
        bankNames.put("UBIN", "Union Bank of India");
        bankNames.put("BARB", "Bank of Baroda");

        return bankNames.getOrDefault(bankCode, "Unknown Bank");
    }
}