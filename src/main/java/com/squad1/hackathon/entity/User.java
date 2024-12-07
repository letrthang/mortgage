package com.squad1.hackathon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded=true)
@Getter
@Setter
public class User {
    @Id
    @Column(name = "email", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;

}