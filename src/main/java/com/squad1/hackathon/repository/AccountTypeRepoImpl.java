package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.AccountType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountTypeRepoImpl {

    private final AccountTypeRepo repository;

    public AccountTypeRepoImpl(AccountTypeRepo repository) {
        this.repository = repository;
    }

    public AccountType save(AccountType accountType) {
        return repository.save(accountType);
    }

    public Optional<AccountType> findById(Integer id) {
        return repository.findById(id);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);

    }
}
