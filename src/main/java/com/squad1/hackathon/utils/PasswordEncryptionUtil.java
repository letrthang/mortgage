package com.squad1.hackathon.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptionUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Encrypt password
    public static String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Verify password
    public static boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }
}

