package com.squad1.hackathon.entity;

import jakarta.persistence.*;



@Entity
@Table(name = "saving_details")
public class SavingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "account_account_type_id", referencedColumnName = "id")
    private AccountAccountType accountAccountType;  // Many SavingDetails can refer to one AccountAccountType

    @Column(name = "balance", nullable = false)
    private Double balance;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountAccountType getAccountAccountType() {
        return accountAccountType;
    }

    public void setAccountAccountType(AccountAccountType accountAccountType) {
        this.accountAccountType = accountAccountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
