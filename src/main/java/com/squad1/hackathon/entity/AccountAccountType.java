package com.squad1.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "account_account_type")
public class AccountAccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountAccountTypeId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private AccountType AccountId;

    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "accountId")
    private AccountType AccountTypeId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AccountAccountType that = (AccountAccountType) o;
        return getAccountAccountTypeId() != null && Objects.equals(getAccountAccountTypeId(), that.getAccountAccountTypeId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
