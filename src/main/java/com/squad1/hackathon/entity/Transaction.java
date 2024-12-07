package com.squad1.hackathon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @Column(name = "transaction_id", unique = true, nullable = false)
    private int id;

    @Column(name = "from")
    private Integer from;

    @Column(name = "to")
    private Integer to;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "remark")
    private String remark;
}