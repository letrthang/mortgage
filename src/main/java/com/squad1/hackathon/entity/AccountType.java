package com.squad1.hackathon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "account_type")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class AccountType {
    @Id
    @Column(name = "account_id", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer accountId;
    @Column(name = "account_name")
    private String accountName;

}