package com.squad1.hackathon.repository;

import com.squad1.hackathon.entity.MortgageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MortgageDetailRepository extends JpaRepository<MortgageDetail, Integer> {
    Optional<MortgageDetail> findByAccountNumber(String accountNumber);
}