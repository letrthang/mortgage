package com.squad1.hackathon;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.squad1.hackathon.api.TransactionServices;
import com.squad1.hackathon.api.TransactionServicesImpl;
import com.squad1.hackathon.dto.TransactionDTO;
import com.squad1.hackathon.entity.Account;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.entity.SavingDetails;
import com.squad1.hackathon.entity.Transaction;
import com.squad1.hackathon.repository.AccountRepository;
import com.squad1.hackathon.repository.MortgageDetailRepository;
import com.squad1.hackathon.repository.SavingDetailsRepository;
import com.squad1.hackathon.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionServicesImpl transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MortgageDetailRepository mortgageDetailRepository;

    @Mock
    private SavingDetailsRepository savingDetailRepository;

    private TransactionDTO transactionDTO;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        transactionDTO = new TransactionDTO();
        transactionDTO.setCustomerId("customer123");
        transactionDTO.setAccountNumberFrom("fromAccount");
        transactionDTO.setAccountNumberFromType("SAVING");
        transactionDTO.setAccountNumberTo("toAccount");
        transactionDTO.setAmount(100.0);
        transactionDTO.setRemark("Test transaction");
    }

    @Test
    public void testTransferFunds_Success_ToSavingAccount() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(200.0);
        fromSavingDetail.setAccountAccountTypeId(1);
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Mocking the to saving account details
        SavingDetails toSavingDetail = new SavingDetails();
        toSavingDetail.setAccountNumber("toAccount");
        toSavingDetail.setBalance(50.0);
        toSavingDetail.setAccountAccountTypeId(1);
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberTo()))
                .thenReturn(Optional.of(toSavingDetail));

        transactionDTO.setAccountNumberToType("SAVING");

        // Call the method
        TransactionDTO result = transactionService.transferFunds(transactionDTO);

        // Assertions
        assertEquals("Test transaction", result.getRemark());
        assertEquals(100.0, fromSavingDetail.getBalance()); // From account balance after deduction
        assertEquals(150.0, toSavingDetail.getBalance()); // To account balance after addition

        // Verify that the transaction was saved
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransferFunds_Success_ToMortgageAccount() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(200.0);
        fromSavingDetail.setAccountAccountTypeId(1);
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Mocking the to mortgage account details
        MortgageDetail toMortgageDetail = new MortgageDetail();
        toMortgageDetail.setAccountNumber("toAccount");
        toMortgageDetail.setBalance(300.0);
        toMortgageDetail.setAccountAccountTypeId(2);
        when(mortgageDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberTo()))
                .thenReturn(Optional.of(toMortgageDetail));

        // Change the to account type to MORTGAGE
        transactionDTO.setAccountNumberToType("MORTGAGE");

        // Call the method
        TransactionDTO result = transactionService.transferFunds(transactionDTO);

        // Assertions
        assertEquals("Test transaction", result.getRemark());
        assertEquals(100.0, fromSavingDetail.getBalance()); // From account balance after deduction
        assertEquals(200.0, toMortgageDetail.getBalance()); // To account balance after addition

        // Verify that the transaction was saved
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransferFunds_InsufficientFunds() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details with insufficient funds
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(50.0); // Less than the transfer amount
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("Insufficient funds in the saving account", exception.getMessage());
    }

    @Test
    public void testTransferFunds_FromMortgageAccount() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Set the from account type to MORTGAGE
        transactionDTO.setAccountNumberFromType("MORTGAGE");

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("Cannot send funds from MORTGAGE Account", exception.getMessage());
    }

    @Test
    public void testTransferFunds_InvalidFromAccountType() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Set an invalid from account type
        transactionDTO.setAccountNumberFromType("INVALID_TYPE");

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("Invalid account type for the from account", exception.getMessage());
    }

    @Test
    public void testTransferFunds_InvalidToAccountType() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(200.0);
        fromSavingDetail.setAccountAccountTypeId(1);

        transactionDTO.setAccountNumberToType("TEST");

        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("Invalid account type for the to account", exception.getMessage());
    }

    @Test
    public void testTransferFunds_ToAccountNotFound() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(200.0);
        fromSavingDetail.setAccountAccountTypeId(1);

        transactionDTO.setAccountNumberToType("SAVING");

        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Mocking the to account retrieval to return empty
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberTo()))
                .thenReturn(Optional.empty());

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("To saving account not found", exception.getMessage());
    }

    @Test
    public void testTransferFunds_ToMortgageAccountNotFound() {
        // Mocking the account retrieval
        Account account = new Account();
        account.setAccountId(1);
        when(accountRepository.findByCustomerId(transactionDTO.getCustomerId())).thenReturn(Optional.of(account));

        // Mocking the saving account details
        SavingDetails fromSavingDetail = new SavingDetails();
        fromSavingDetail.setAccountNumber("fromAccount");
        fromSavingDetail.setBalance(200.0);
        fromSavingDetail.setAccountAccountTypeId(1);
        when(savingDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberFrom()))
                .thenReturn(Optional.of(fromSavingDetail));

        // Set the to account type to MORTGAGE
        transactionDTO.setAccountNumberToType("MORTGAGE");

        // Mocking the to account retrieval to return empty
        when(mortgageDetailRepository.findByAccountNumber(transactionDTO.getAccountNumberTo()))
                .thenReturn(Optional.empty());

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionDTO);
        });

        assertEquals("To mortgage account not found", exception.getMessage());
    }
}
