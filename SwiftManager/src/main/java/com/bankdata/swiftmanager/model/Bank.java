package com.bankdata.swiftmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name="banks")
public class Bank {
    @Id
    @Column(name="swift_code")
    private String SWIFTCode;
    @Column(nullable = false)
    private String bankName;
    private String address;
    @Column(nullable = false)
    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "country_ISO2", nullable = false)
    private Country country;

    public Bank() {}

    public Bank(String SWIFTCode, String bankName, String address, boolean isHeadquarter, Country country) {
        this.SWIFTCode = SWIFTCode;
        this.bankName = bankName;
        this.address = address;
        this.isHeadquarter = isHeadquarter;
        this.country = country;
    }

    public String getSWIFTCode() {
        return SWIFTCode;
    }

    public void setSWIFTCode(String SWIFTCode) {
        this.SWIFTCode = SWIFTCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
