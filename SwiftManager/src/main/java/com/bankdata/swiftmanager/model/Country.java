package com.bankdata.swiftmanager.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="countries")
public class Country {
    @Id
    @Column(name="country_ISO2", length=2)
    private String countryISO2Code;
    @Column(nullable=false, unique=true)
    private String countryName;
    @OneToMany(mappedBy = "country")
    private Set<Bank> items;

    public Country() {}

    public Country(String countryISO2Code, String countryName, Set<Bank> items) {
        this.countryISO2Code = countryISO2Code;
        this.countryName = countryName;
        this.items = items;
    }

    public String getCountryISO2Code() {
        return countryISO2Code;
    }

    public void setCountryISO2Code(String countryISO2Code) {
        this.countryISO2Code = countryISO2Code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Set<Bank> getItems() {
        return items;
    }

    public void setItems(Set<Bank> items) {
        this.items = items;
    }
}
