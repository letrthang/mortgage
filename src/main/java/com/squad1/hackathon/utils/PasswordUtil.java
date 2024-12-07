package com.squad1.hackathon.utils;

import java.security.SecureRandom;

public class PasswordUtil {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}";

    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }

        return password.toString();
    }
}
