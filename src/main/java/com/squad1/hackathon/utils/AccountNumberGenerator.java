package com.squad1.hackathon.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountNumberGenerator {
    private AccountNumberGenerator() {
    }

    public static String generateMortgageAccountNumber() {
        // Generate a UUID, take only numeric parts, and limit to 12 digits
        return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 12);
    }
}
