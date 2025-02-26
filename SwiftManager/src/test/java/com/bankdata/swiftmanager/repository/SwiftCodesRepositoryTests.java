package com.bankdata.swiftmanager.repository;

import com.bankdata.swiftmanager.exception.SWIFTCodeNotFoundException;
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
        country = Country.builder().countryISO2("PL").countryName("Poland").build();
        countriesRepository.save(country);
    }

    @Test
    @DisplayName("JUnit test for save bank operation.")
    public void givenBankObject_whenSave_thenReturnSavedBank() {
        // given
        Bank bank = Bank.builder()
                    .swiftCode("PKOPPLPW")
                    .bankName("PKO Bank Polski")
                    .address("Warsaw, Poland")
                    .isHeadquarter(false)
                    .country(country)
                    .build();
        // when
        Bank savedBank = swiftCodesRepository.save(bank);
        // then
        assertNotNull(savedBank);
    }

    @Test
    @DisplayName("JUnit test for find bank by ID.")
    public void givenBankObject_whenFindById_thenReturnFoundBank() {
        // given
        Bank bankOne = Bank.builder()
                .swiftCode("BHJIUYTFXXX")
                .bankName("Bank Test 1")
                .address("Warsaw, Poland")
                .isHeadquarter(true)
                .country(country)
                .build();

        Bank bankTwo = Bank.builder()
                .swiftCode("HYGBCSCS")
                .bankName("Bank Test 2")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();

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
        Bank bankOne = Bank.builder()
                .swiftCode("BHJIUYTFXXX")
                .bankName("Bank Test 1")
                .address("Warsaw, Poland")
                .isHeadquarter(true)
                .country(country)
                .build();
        Bank bankTwo = Bank.builder()
                .swiftCode("HYGBCSCS")
                .bankName("Bank Test 2")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();

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
        Bank headquarter = Bank.builder()
                .swiftCode("BHJIUYTFXXX")
                .bankName("Bank Test 1")
                .address("Warsaw, Poland")
                .isHeadquarter(true)
                .country(country)
                .build();
        Bank branchOne = Bank.builder()
                .swiftCode("BHJIUYTFYHS")
                .bankName("Bank Test 2")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        Bank branchTwo = Bank.builder()
                .swiftCode("BHJIUYTFTGF")
                .bankName("Bank Test 3")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        Bank otherBranch = Bank.builder()
                .swiftCode("KHKIUYTFTGF")
                .bankName("Bank Test 4")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        swiftCodesRepository.save(headquarter);
        swiftCodesRepository.save(branchOne);
        swiftCodesRepository.save(branchTwo);
        swiftCodesRepository.save(otherBranch);
        // when
        List<Bank> branches = swiftCodesRepository.findBranchesByHeadquarter(headquarter.getSwiftCode().substring(0, 8));
        // then
        assertEquals(branches.size(), 2);
    }

    @Test
    @DisplayName("JUnit test for delete existing bank.")
    public void givenBankSwiftCode_whenDeleteById_thenReturnDeletedBank() {
        // given
        Bank bank = Bank.builder()
                .swiftCode("KHKIUYTFTGF")
                .bankName("Bank Test 3")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        swiftCodesRepository.save(bank);
        // when
        swiftCodesRepository.deleteById("BHJIUYTFXXX");
        // then
        Optional<Bank> deletedBank = swiftCodesRepository.findById("BHJIUYTFXXX");
        assertFalse(deletedBank.isPresent());
    }
}
