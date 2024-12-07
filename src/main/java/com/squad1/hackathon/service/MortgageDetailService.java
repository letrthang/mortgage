package com.squad1.hackathon.service;

import com.squad1.hackathon.dto.MortgageDetailRequest;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.repository.MortgageDetailRepository;
import com.squad1.hackathon.utils.AccountNumberGenerator;
import com.squad1.hackathon.utils.PasswordEncryptionUtil;
import com.squad1.hackathon.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MortgageDetailService {
    @Autowired
    private MortgageDetailRepository mortgageDetailRepository;

    /**
     *  It will validate the mortgage deposit validation and generate random account and password for mortgage
     * @param mortgageDetail
     * @return MortgageDetail
     */
    public MortgageDetail createMortgage(MortgageDetailRequest mortgageDetail) {
        // Validate property cost and deposit amount
        double propertyCost = mortgageDetail.getPropertyCost();
        double depositAmount = mortgageDetail.getDepositAmount();
        double minimumDeposit = propertyCost * 0.20;

        if (depositAmount < minimumDeposit) {
            throw new IllegalArgumentException("Deposit must be at least 20% of the property cost");
        }

        MortgageDetail mortgageDetails = new MortgageDetail();
        String mortgagePassword = PasswordUtil.generateRandomPassword(10);
        System.out.println("plain password for testing " + mortgagePassword);
        // Calculate balance (property cost - deposit)
        double balance = propertyCost - depositAmount;
        mortgageDetails.setPropertyCost(propertyCost);
        mortgageDetails.setAccountAccountTypeId(2);
        mortgageDetails.setAccountNumber(AccountNumberGenerator.generateMortgageAccountNumber());
        mortgageDetails.setMortgageType("Buy new property");
        mortgageDetails.setMortgagePassword(PasswordEncryptionUtil.encryptPassword(mortgagePassword));
        mortgageDetails.setBalance(balance);

        System.out.println("Encrypted password for testing " + mortgageDetails.getMortgagePassword());
        // Save mortgage detail
        mortgageDetails = mortgageDetailRepository.save(mortgageDetails);
        mortgageDetails.setMortgagePassword(mortgagePassword);
        return mortgageDetails;
    }

    /**
     * It will validate the mortgage login with account number and password
     * User passes the string password and will compare with encrypted database password
     * @param accountNumber
     * @param rawPassword
     * @return String
     */
    public String validateLogin(String accountNumber, String rawPassword) {
        Optional<MortgageDetail> optionalMortgage = mortgageDetailRepository.findByAccountNumber(accountNumber);

        if (!optionalMortgage.isPresent()) {
            throw new IllegalArgumentException("No mortgage account exists for the provided account number.");
        }

        MortgageDetail mortgageDetail = optionalMortgage.get();

        if (!PasswordEncryptionUtil.verifyPassword(rawPassword, mortgageDetail.getMortgagePassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return "Login successful!";
    }
}
