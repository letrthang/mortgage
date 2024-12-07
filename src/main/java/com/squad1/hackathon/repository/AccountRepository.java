package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}