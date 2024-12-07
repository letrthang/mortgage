package com.squad1.hackathon.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testGenerateRandomPassword_Length() {
        int length = 12;
        String password = PasswordUtil.generateRandomPassword(length);

        assertNotNull(password, "Generated password should not be null.");
        assertEquals(length, password.length(), "Password length should match the requested length.");
    }

    @Test
    public void testGenerateRandomPassword_ContainsUppercase() {
        String password = PasswordUtil.generateRandomPassword(20);

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        assertTrue(hasUppercase, "Password should contain at least one uppercase letter.");
    }

    @Test
    public void testGenerateRandomPassword_ContainsLowercase() {
        String password = PasswordUtil.generateRandomPassword(20);

        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        assertTrue(hasLowercase, "Password should contain at least one lowercase letter.");
    }

    @Test
    public void testGenerateRandomPassword_ContainsDigit() {
        String password = PasswordUtil.generateRandomPassword(20);

        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        assertTrue(hasDigit, "Password should contain at least one digit.");
    }

    @Test
    public void testGenerateRandomPassword_ContainsSpecialCharacter() {
        String password = PasswordUtil.generateRandomPassword(20);

        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()-_=+[]{}".indexOf((char) ch) >= 0);
        assertTrue(hasSpecialChar, "Password should contain at least one special character.");
    }

    @Test
    public void testGenerateRandomPassword_UniquePasswords() {
        String password1 = PasswordUtil.generateRandomPassword(20);
        String password2 = PasswordUtil.generateRandomPassword(20);

        assertNotEquals(password1, password2, "Generated passwords should be unique.");
    }
}
