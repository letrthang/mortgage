package com.squad1.hackathon.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountNumberGeneratorTest {

    @Test
    public void testGenerateMortgageAccountNumber_Length() {
        String accountNumber = AccountNumberGenerator.generateMortgageAccountNumber();

        assertNotNull(accountNumber, "Generated account number should not be null.");
        assertEquals(12, accountNumber.length(), "Generated account number should have exactly 12 digits.");
    }

    @Test
    public void testGenerateMortgageAccountNumber_OnlyNumeric() {
        String accountNumber = AccountNumberGenerator.generateMortgageAccountNumber();

        boolean isNumeric = accountNumber.matches("[0-9]+");
        assertTrue(isNumeric, "Generated account number should contain only numeric digits.");
    }

    @Test
    public void testGenerateMortgageAccountNumber_Unique() {
        String accountNumber1 = AccountNumberGenerator.generateMortgageAccountNumber();
        String accountNumber2 = AccountNumberGenerator.generateMortgageAccountNumber();

        assertNotEquals(accountNumber1, accountNumber2, "Generated account numbers should be unique.");
    }

    @Test
    public void testGenerateMortgageAccountNumber_ValidFormat() {
        String accountNumber = AccountNumberGenerator.generateMortgageAccountNumber();

        assertEquals(12, accountNumber.length(), "Account number should be exactly 12 digits.");
        assertTrue(accountNumber.matches("[0-9]{12}"), "Account number should contain exactly 12 digits.");
    }
}

