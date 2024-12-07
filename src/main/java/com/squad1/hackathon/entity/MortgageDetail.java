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
@Table(name = "mortgage_detail")
public class MortgageDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mortgageDetailId;
    private String accountNumber;
    private Integer accountAccountTypeId;
    private Double propertyCost;
    private Double balance;
    private String mortgagePassword;
    private String mortgageType;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MortgageDetail that = (MortgageDetail) o;
        return getMortgageDetailId() != null && Objects.equals(getMortgageDetailId(), that.getMortgageDetailId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}