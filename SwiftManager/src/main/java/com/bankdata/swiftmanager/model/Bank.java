package com.bankdata.swiftmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="banks")
public class Bank {
    @Id
    private String swiftCode;
    @Column(nullable = false)
    private String bankName;
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(swiftCode, bank.swiftCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(swiftCode);
    }

    @Column(nullable = false)
    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "country_ISO2", nullable = false)
    private Country country;

}
