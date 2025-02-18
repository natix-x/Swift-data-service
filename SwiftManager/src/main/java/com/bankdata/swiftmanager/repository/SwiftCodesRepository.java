package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodesRepository extends JpaRepository<Bank, String> {
    List<Bank> findByCountry_CountryISO2(String countryISO2);

    @Query("SELECT b FROM Bank b WHERE b.swiftCode LIKE CONCAT(:pattern, '%')")
    List<Bank> findBranchesByHeadquarter(@Param("pattern") String pattern);
}
