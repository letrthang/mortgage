package com.squad1.hackathon.service;

import com.squad1.hackathon.entity.Account;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.entity.SavingDetails;
import com.squad1.hackathon.entity.User;
import com.squad1.hackathon.repository.AccountRepository;
import com.squad1.hackathon.repository.MortgageDetailRepository;
import com.squad1.hackathon.repository.SavingDetailsRepository;
import com.squad1.hackathon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SavingDetailsRepository savingDetailRepository;
    @Autowired
    private MortgageDetailRepository mortgageDetailRepository;
    @Autowired
    private AccountRepository accountRepository;

    /**
     * It will get all saving account users
     * @return List<User>
     */
    public List<User> getAllSavingsAccountUsers() {
        return userRepository.findAll();
    }

    /**
     * It will create a mortgage details and validate the deposit
     * @param mortgageDetail
     * @return MortgageDetail
     */
    public MortgageDetail createMortgageDetail(MortgageDetail mortgageDetail) {
        double deposit = mortgageDetail.getPropertyCost() * 0.20;
        double depositProvided = mortgageDetail.getBalance();
        if (depositProvided < deposit) {
            throw new IllegalArgumentException("Deposit must be >= 20% of property cost");
        }
        return mortgageDetailRepository.save(mortgageDetail);
    }

    /**
     * Update saving by account number
     * @param accountNumber
     * @param balance
     * @return SavingDetails
     */
    public SavingDetails updateSavingsAccount(String accountNumber, double balance) {
        SavingDetails savingDetail = savingDetailRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        savingDetail.setBalance(balance);
        return savingDetailRepository.save(savingDetail);

    }

    /**
     * It will link the accounts to customer id
     * @param accountId
     * @param customerId
     * @return Account
     */
    public Account linkAccounts(Integer accountId, String customerId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setCustomerId(customerId);
        return accountRepository.save(account);
    }

}
