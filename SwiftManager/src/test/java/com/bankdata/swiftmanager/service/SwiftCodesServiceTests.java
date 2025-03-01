package com.bankdata.swiftmanager.service;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.exception.SwiftCodeNotFoundException;
import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import com.bankdata.swiftmanager.repository.CountriesRepository;
import com.bankdata.swiftmanager.repository.SwiftCodesRepository;
import com.bankdata.swiftmanager.service.impl.SwiftCodesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwiftCodesServiceTests {

    private Country country;

    @Mock
    private CountriesRepository countriesRepository;

    @Mock
    private SwiftCodesRepository swiftCodesRepository;

    @InjectMocks
    private SwiftCodesServiceImpl swiftCodesService;

    @BeforeEach
    void setUpTestData() {
        country = Country.builder().countryISO2("PL").countryName("Poland").build();
        lenient().when(countriesRepository.findById(country.getCountryISO2())).thenReturn(Optional.of(country));

    }

    @Test
    @DisplayName("JUnit test for add new SWIFT code method.")
    public void givenBranchDto_whenAddNewSwiftCode_thenReturnBank() {
        // given
        Bank bank = Bank.builder()
                .swiftCode("PKOPPLPW")
                .bankName("PKO Bank Polski")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        BranchDTO branchDTO = BranchDTO.builder()
                .address("Warsaw, Poland")
                .bankName("PKO Bank Polski")
                .countryISO2(country.getCountryISO2())
                .isHeadquarter(false)
                .swiftCode("PKOPPLPW")
                .build();

        when(swiftCodesRepository.save(Mockito.any(Bank.class))).thenReturn(bank);


        // when
        swiftCodesService.addSWIFTCode(branchDTO);

        // then
        verify(swiftCodesRepository, Mockito.times(1)).save(Mockito.argThat(addedBank ->
                addedBank.getSwiftCode().equals("PKOPPLPW") &&
                        addedBank.getBankName().equals("PKO Bank Polski")
        ));
    }

    @Test
    @DisplayName("JUnit test for get all banks from country.")
    public void givenCountryISO2_whenGetAllSwiftCodesFromCountryISO2_thenReturnBanksFromCountryDTO() {
        // given
        String countryISO2 = "PL";
        Bank bankOne = Bank.builder()
                .swiftCode("PKOPPLPW")
                .bankName("PKO Bank Polski")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        Bank bankTwo = Bank.builder()
                .swiftCode("ALBPPLPW")
                .bankName("Alior Bank")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();

        List<Bank> banks = Arrays.asList(bankOne, bankTwo);

        when(swiftCodesRepository.findByCountry_CountryISO2(countryISO2)).thenReturn(banks);


        // when
        BanksFromCountryDTO banksFromCountryDTO = swiftCodesService.getAllSwiftCodesFromCountryISO2(countryISO2);

        // then
        verify(swiftCodesRepository, Mockito.times(1)).findByCountry_CountryISO2(countryISO2);
        assertNotNull(banksFromCountryDTO);
        List<BranchDTO> banksInCountry = banksFromCountryDTO.swiftCodes();
        assertEquals(banks.size(), banksInCountry.size());
    }

    @Test
    @DisplayName("JUnit test for delete existing SWIFT code method.")
    public void givenExistingSwiftCode_whenDeleteSwiftCode_thenVerify() {
        // given
        String swiftCode = "PKOPPLPW";
        Bank bank = Bank.builder()
                .swiftCode(swiftCode)
                .bankName("PKO Bank Polski")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();

        when(swiftCodesRepository.findById(swiftCode)).thenReturn(Optional.of(bank));

        // when
        swiftCodesService.deleteSWIFTCode(swiftCode);
        // then
        verify(swiftCodesRepository, Mockito.times(1)).deleteById(swiftCode);
    }

    @Test
    @DisplayName("JUnit test for delete non-existing SWIFT code.")
    public void givenNonExistingSwiftCode_whenDeleteSwiftCode_thenThrowSwiftCodeNotFoundException() {
        // given
        String swiftCode = "PKOPPLPW";

        when(swiftCodesRepository.findById(swiftCode)).thenReturn(Optional.empty());

        // when & then
        assertThrows(SwiftCodeNotFoundException.class, () -> swiftCodesService.deleteSWIFTCode(swiftCode));
    }

    @Test
    @DisplayName("JUnit test for get existing SWIFT code.")
    public void givenExistingSwiftCode_whenGetSwiftCode_thenReturnBankDTO() {
        // given
        String swiftCode = "PKOPPLPW";
        Bank bank = Bank.builder()
                .swiftCode(swiftCode)
                .bankName("PKO Bank Polski")
                .address("Warsaw, Poland")
                .isHeadquarter(false)
                .country(country)
                .build();
        when(swiftCodesRepository.findById(swiftCode)).thenReturn(Optional.of(bank));
        // when
        BankDTO gotBankDTO = swiftCodesService.getSWIFTCodeDetails(swiftCode);
        // then
        assertNotNull(gotBankDTO);
        assertEquals(swiftCode, gotBankDTO.swiftCode());
        assertEquals("PKO Bank Polski", gotBankDTO.bankName());
        assertEquals("Warsaw, Poland", gotBankDTO.address());
        assertFalse(gotBankDTO.isHeadquarter());
    }

    @Test
    @DisplayName("JUnit test for get non-existing SWIFT code.")
    public void givenNonExistingSwiftCode_whenGetSwiftCode_thenReturnBankDTO() {
        // given
        String swiftCode = "PKOPPLPW";

        when(swiftCodesRepository.findById(swiftCode)).thenReturn(Optional.empty());
        // when & then
        assertThrows(SwiftCodeNotFoundException.class, () -> swiftCodesService.getSWIFTCodeDetails(swiftCode));
    }


}
