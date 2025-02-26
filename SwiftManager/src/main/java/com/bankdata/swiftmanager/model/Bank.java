package com.bankdata.swiftmanager.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "country_ISO2", nullable = false)
    private Country country;

}
