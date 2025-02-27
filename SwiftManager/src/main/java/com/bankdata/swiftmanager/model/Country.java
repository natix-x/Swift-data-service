package com.bankdata.swiftmanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="countries")
public class Country {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(countryISO2, country.countryISO2);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(countryISO2);
    }

    @Id
    @Column(name="country_ISO2", length=2)
    private String countryISO2;
    @Column(nullable=false, unique=true)
    private String countryName;
    @OneToMany(mappedBy = "country")
    private Set<Bank> banks;
}
