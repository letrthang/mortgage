package com.squad1.hackathon.api;

import ch.qos.logback.core.util.StringUtil;
import com.squad1.hackathon.dto.TransactionDTO;
import com.squad1.hackathon.entity.*;
import com.squad1.hackathon.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionServicesImpl implements TransactionServices {

    @Autowired
    private AccountAccountTypeRepository accountAccountTypeRepository;

    @Autowired
    private MortgageDetailRepository mortgageDetailRepository;

    @Autowired
    private SavingDetailsRepository savingDetailRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private static final String MORTGAGE_ACCOUNT = "MORTGAGE";
    private static final String SAVING_ACCOUNT = "SAVING";

    @Transactional
    @Override
    public TransactionDTO transferFunds(TransactionDTO transactionDTO) {
        // Retrieve the account based on customer ID
        Optional<Account> accountOptional = accountRepository.findByCustomerId(transactionDTO.getCustomerId());
        Account account = accountOptional.orElseThrow(() -> new RuntimeException("Customer not found"));

        // Determine the account type for the "from" account
        String fromAccountType = transactionDTO.getAccountNumberFromType();
        String fromAccountNumber = transactionDTO.getAccountNumberFrom();

        Integer from, to;

        if (MORTGAGE_ACCOUNT.equalsIgnoreCase(fromAccountType)) {

            throw new RuntimeException("Cannot send funds from MORTGAGE Account");

        } else if (SAVING_ACCOUNT.equalsIgnoreCase(fromAccountType)) {
            SavingDetails savingDetail = savingDetailRepository.findByAccountNumber(fromAccountNumber)
                    .orElseThrow(() -> new RuntimeException("From saving account not found"));

            // Check if sufficient balance is available
            if (savingDetail.getBalance() < transactionDTO.getAmount()) {
                throw new RuntimeException("Insufficient funds in the saving account");
            }

            // Deduct the amount from the saving account
            savingDetail.setBalance(savingDetail.getBalance() - transactionDTO.getAmount());
            savingDetailRepository.save(savingDetail);

            from = savingDetail.getAccountAccountTypeId();
        } else {
            throw new RuntimeException("Invalid account type for the from account");
        }

        // Determine the account type for the "to" account
        String toAccountType = transactionDTO.getAccountNumberToType();
        String toAccountNumber = transactionDTO.getAccountNumberTo();

        // Retrieve the "to" account based on its type
        if (SAVING_ACCOUNT.equalsIgnoreCase(toAccountType)) {
            SavingDetails toSavingDetail = savingDetailRepository.findByAccountNumber(toAccountNumber)
                    .orElseThrow(() -> new RuntimeException("To saving account not found"));

            // Add funds to the "to" saving account
            toSavingDetail.setBalance(toSavingDetail.getBalance() + transactionDTO.getAmount());
            savingDetailRepository.save(toSavingDetail);
            to = toSavingDetail.getAccountAccountTypeId();
        } else if (MORTGAGE_ACCOUNT.equalsIgnoreCase(toAccountType)) {
            MortgageDetail toMortgageDetail = mortgageDetailRepository.findByAccountNumber(toAccountNumber)
                    .orElseThrow(() -> new RuntimeException("To mortgage account not found"));

            // Add funds to the "to" mortgage account
            toMortgageDetail.setBalance(toMortgageDetail.getBalance() - transactionDTO.getAmount());
            mortgageDetailRepository.save(toMortgageDetail);
            to = toMortgageDetail.getAccountAccountTypeId();
        } else {
            throw new RuntimeException("Invalid account type for the to account");
        }

        Transaction transaction = new Transaction();
        transaction.setTo(to);
        transaction.setFrom(from);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setRemark(transactionDTO.getRemark());
        transactionRepository.save(transaction);

        return transactionDTO; // Return the updated TransactionDTO
    }



}
