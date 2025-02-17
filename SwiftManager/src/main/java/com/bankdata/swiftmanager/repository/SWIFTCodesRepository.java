package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SWIFTCodesRepository extends JpaRepository<Bank, String> {
    List<Bank> findByCountry_CountryISO2(String countryISO2);

    @Query("SELECT b FROM Bank b WHERE b.SWIFTCode LIKE CONCAT(:pattern, '%')")
    List<Bank> findBranchesByHeadquarter(@Param("pattern") String pattern);
}
