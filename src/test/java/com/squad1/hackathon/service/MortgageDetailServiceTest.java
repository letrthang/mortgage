package com.squad1.hackathon.service;

import com.squad1.hackathon.dto.MortgageDetailRequest;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.repository.MortgageDetailRepository;
import com.squad1.hackathon.utils.PasswordEncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MortgageDetailServiceTest {

    @Mock
    private MortgageDetailRepository mortgageDetailRepository;

    @InjectMocks
    private MortgageDetailService mortgageDetailService;

    private MortgageDetailRequest mortgageDetailRequest;
    private MortgageDetail mortgageDetail;

    @BeforeEach
    public void setUp() {
        // Setting up data for the tests
        mortgageDetailRequest = new MortgageDetailRequest();
        mortgageDetailRequest.setPropertyCost(500000.0);
        mortgageDetailRequest.setDepositAmount(100000.0);

        mortgageDetail = new MortgageDetail();
        mortgageDetail.setPropertyCost(500000.0);
        mortgageDetail.setBalance(100000.0);
        mortgageDetail.setAccountNumber("123456789012");
        mortgageDetail.setBalance(400000.0);
        mortgageDetail.setMortgageType("Buy new property");
    }

    @Test
    public void testCreateMortgage_ValidDeposit() {
        // Arrange
        when(mortgageDetailRepository.save(Mockito.any(MortgageDetail.class))).thenReturn(mortgageDetail);

        // Act
        MortgageDetail createdMortgageDetail = mortgageDetailService.createMortgage(mortgageDetailRequest);

        // Assert
        assertNotNull(createdMortgageDetail, "Mortgage detail should be created.");
        assertEquals(500000, createdMortgageDetail.getPropertyCost(), "Property cost should match.");
        assertEquals(400000, createdMortgageDetail.getBalance(), "Balance should be correct after deposit.");
        assertEquals("Buy new property", createdMortgageDetail.getMortgageType(), "Mortgage type should be 'Buy new property'.");
        assertTrue(createdMortgageDetail.getAccountNumber().matches("\\d{12}"), "Account number should be 12 digits.");
        assertNotNull(createdMortgageDetail.getMortgagePassword(), "Mortgage password should be encrypted.");
    }

    @Test
    public void testCreateMortgage_InvalidDeposit() {
        // Arrange
        mortgageDetailRequest.setDepositAmount(90000.0);  // Less than 20% of property cost

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mortgageDetailService.createMortgage(mortgageDetailRequest);
        });
        assertEquals("Deposit must be at least 20% of the property cost", thrown.getMessage());
    }

    @Test
    public void testValidateLogin_SuccessfulLogin() {
        // Arrange
        mortgageDetail.setMortgagePassword(PasswordEncryptionUtil.encryptPassword("validPassword"));
        when(mortgageDetailRepository.findByAccountNumber("123456789012")).thenReturn(Optional.of(mortgageDetail));

        // Act
        String result = mortgageDetailService.validateLogin("123456789012", "validPassword");

        // Assert
        assertEquals("Login successful!", result, "Login should be successful.");
    }

    @Test
    public void testValidateLogin_InvalidAccountNumber() {
        // Arrange
        when(mortgageDetailRepository.findByAccountNumber("invalidAccount")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mortgageDetailService.validateLogin("invalidAccount", "somePassword");
        });
        assertEquals("No mortgage account exists for the provided account number.", thrown.getMessage());
    }

    @Test
    public void testValidateLogin_InvalidPassword() {
        // Arrange
        mortgageDetail.setMortgagePassword(PasswordEncryptionUtil.encryptPassword("validPassword"));
        when(mortgageDetailRepository.findByAccountNumber("123456789012")).thenReturn(Optional.of(mortgageDetail));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mortgageDetailService.validateLogin("123456789012", "wrongPassword");
        });
        assertEquals("Invalid password.", thrown.getMessage());
    }
}
