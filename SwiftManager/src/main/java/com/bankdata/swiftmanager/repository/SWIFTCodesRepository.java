package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SWIFTCodesRepository extends JpaRepository<Bank, String> {
    List<Bank> findByCountryISO2(String countryISO2);
}
