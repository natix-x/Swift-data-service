package com.bankdata.swiftmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="countries")
public class Country {
    @Id
    @Column(name="country_ISO2", length=2)
    private String countryISO2;
    @Column(nullable=false, unique=true)
    private String countryName;
    @OneToMany(mappedBy = "country")
    private Set<Bank> banks;
}
