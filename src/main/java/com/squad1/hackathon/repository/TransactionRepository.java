package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}