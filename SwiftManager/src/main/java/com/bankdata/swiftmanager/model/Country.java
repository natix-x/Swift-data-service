package com.bankdata.swiftmanager.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="countries")
public class Country {
    @Id
    @Column(name="country_ISO2")
    private String countryISO2;
    @Column(nullable=false, unique=true)
    private String countryName;
    @OneToMany(mappedBy = "country")
    private Set<Bank> banks;

    public Country() {}

    public Country(String countryISO2, String countryName, Set<Bank> banks) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.banks = banks;
    }


    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2Code) {
        this.countryISO2 = countryISO2Code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Set<Bank> getBanks() {
        return banks;
    }

    public void setBanks(Set<Bank> items) {
        this.banks = items;
    }
}
