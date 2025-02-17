package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends JpaRepository<Country, String> {
}
