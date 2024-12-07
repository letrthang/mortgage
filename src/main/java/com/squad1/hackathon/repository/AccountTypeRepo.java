package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepo extends JpaRepository<AccountType, Integer> {
}
