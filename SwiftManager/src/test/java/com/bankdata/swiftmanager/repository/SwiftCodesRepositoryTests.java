package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SwiftCodesRepositoryTests {

    @Autowired
    private SwiftCodesRepository swiftCodesRepository;

    private Country country;
    @Autowired
    private CountriesRepository countriesRepository;

    @BeforeEach
    void setUpTestData() {
        country = new Country();
        country.setCountryISO2("PL");
        country.setCountryName("Poland");
        countriesRepository.save(country);
    }

    @Test
    @DisplayName("JUnit test for save bank operation.")
    public void givenBankObject_whenSave_thenReturnSavedBank() {
        // given
        Bank bank = new Bank("PKOPPLPW", "PKO Bank Polski", "Warsaw, Poland", false, country);
        // when
        Bank savedBank = swiftCodesRepository.save(bank);
        // then
        assertNotNull(savedBank);
    }

    @Test
    @DisplayName("JUnit test for find bank by ID.")
    public void givenBankObject_whenFindById_thenReturnFoundBank() {
        // given
        Bank bankOne = new Bank("BHJIUYTFXXX", "Bank Test 1", "Warsaw, Poland", true, country);
        Bank bankTwo = new Bank("HYGBCSCS", "Bank Test 2", "Warsaw, Poland", false, country);
        swiftCodesRepository.save(bankOne);
        swiftCodesRepository.save(bankTwo);
        // when
        Optional<Bank> foundBankOne = swiftCodesRepository.findById("BHJIUYTFXXX");
        Optional<Bank> foundBankTwo = swiftCodesRepository.findById("HYGBCSCS");
        // then
        assertTrue(foundBankOne.isPresent());
        assertTrue(foundBankTwo.isPresent());
        assertEquals(foundBankOne.get().getBankName(), bankOne.getBankName());
        assertEquals(foundBankTwo.get().getBankName(), bankTwo.getBankName());
    }

    @Test
    @DisplayName("JUnit test for find banks by CountryISO2")
    public void givenCountryISO2_whenFindByCountryISO2_thenReturnFoundBanks() {
        // given
        Bank bankOne = new Bank("BHJIUYTFXXX", "Bank Test 1", "Warsaw, Poland", true, country);
        Bank bankTwo = new Bank("HYGBCSCS", "Bank Test 2", "Warsaw, Poland", false, country);
        swiftCodesRepository.save(bankOne);
        swiftCodesRepository.save(bankTwo);
        // then
        List<Bank> banks = swiftCodesRepository.findByCountry_CountryISO2("PL");
        assertEquals(banks.size(), 2);
    }

    @Test
    @DisplayName("JUnit test for find no banks with provided CountryISO2")
    public void givenCountryISO2_whenFindByCountryISO2_thenReturnNoBanksInThatCountry() {
        // when
        List<Bank> banks = swiftCodesRepository.findByCountry_CountryISO2("PL");
        // then
        assertEquals(banks.size(), 0);
    }

    @Test
    @DisplayName("JUnit test for finding branches by headquarter.")
    public void givenHeadquarterSwiftCode_whenFindByHeadquarter_thenReturnBranches() {
        // given
        Bank headquarter = new Bank("BHJIUYTFXXX", "Bank Test 1", "Warsaw, Poland", true, country);
        Bank branchOne = new Bank("BHJIUYTFYHS", "Bank Test 2", "Warsaw, Poland", false, country);
        Bank branchTwo = new Bank("BHJIUYTFPOS", "Bank Test 3", "Warsaw, Poland", false, country);
        Bank branchThree = new Bank("BHJIUYTF987", "Bank Test 2", "Warsaw, Poland", false, country);
        swiftCodesRepository.save(headquarter);
        swiftCodesRepository.save(branchOne);
        swiftCodesRepository.save(branchTwo);
        swiftCodesRepository.save(branchThree);
        // when
        List<Bank> branches = swiftCodesRepository.findBranchesByHeadquarter(headquarter.getSwiftCode().substring(0, 8));
        // then
        assertEquals(branches.size(), 3);
    }

}
