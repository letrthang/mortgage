package com.squad1.hackathon.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncryptionUtilTest {

    @Test
    public void testEncryptPassword_NotNull() {
        String rawPassword = "MySecurePassword123!";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        assertNotNull(encryptedPassword, "Encrypted password should not be null.");
    }

    @Test
    public void testEncryptPassword_DifferentFromRaw() {
        String rawPassword = "MySecurePassword123!";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        assertNotEquals(rawPassword, encryptedPassword, "Encrypted password should not match the raw password.");
    }

    @Test
    public void testVerifyPassword_Matching() {
        String rawPassword = "MySecurePassword123!";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        boolean isMatching = PasswordEncryptionUtil.verifyPassword(rawPassword, encryptedPassword);
        assertTrue(isMatching, "Password verification should return true for a matching password.");
    }

    @Test
    public void testVerifyPassword_NotMatching() {
        String rawPassword = "MySecurePassword123!";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        boolean isNotMatching = PasswordEncryptionUtil.verifyPassword("WrongPassword", encryptedPassword);
        assertFalse(isNotMatching, "Password verification should return false for a non-matching password.");
    }

    @Test
    public void testVerifyPassword_MultipleEncryptions() {
        String rawPassword = "MySecurePassword123!";
        String encryptedPassword1 = PasswordEncryptionUtil.encryptPassword(rawPassword);
        String encryptedPassword2 = PasswordEncryptionUtil.encryptPassword(rawPassword);

        assertNotEquals(encryptedPassword1, encryptedPassword2, "Encrypted passwords should differ due to salt.");
        assertTrue(PasswordEncryptionUtil.verifyPassword(rawPassword, encryptedPassword1), "Password verification should succeed for the first encrypted password.");
        assertTrue(PasswordEncryptionUtil.verifyPassword(rawPassword, encryptedPassword2), "Password verification should succeed for the second encrypted password.");
    }
}
