package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.SavingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingDetailsRepository extends JpaRepository<SavingDetails, Integer> {
    Optional<SavingDetails> findByAccountNumber(String accountNumber);
}