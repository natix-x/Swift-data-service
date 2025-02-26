package com.bankdata.swiftmanager.service.impl;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.exception.CountryNotFoundException;
import com.bankdata.swiftmanager.exception.SWIFTCodeNotFoundException;
import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import com.bankdata.swiftmanager.repository.CountriesRepository;
import com.bankdata.swiftmanager.repository.SwiftCodesRepository;
import com.bankdata.swiftmanager.service.SWiftCodesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwiftCodesServiceImpl implements SWiftCodesService {

    private final SwiftCodesRepository swiftCodesRepository;

    private final CountriesRepository countriesRepository;

    public SwiftCodesServiceImpl(SwiftCodesRepository swiftCodesRepository, CountriesRepository countriesRepository) {
        this.swiftCodesRepository = swiftCodesRepository;
        this.countriesRepository = countriesRepository;
    }

    @Override
    public BankDTO getSWIFTCodeDetails(String SWIFTCode) {
        Bank bank = swiftCodesRepository.findById(SWIFTCode).orElseThrow(() -> new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."));
        if (!bank.isHeadquarter()) {
            Country bankCountry = bank.getCountry();
            return new BankDTO(bank.getAddress(), bank.getBankName(), bankCountry.getCountryISO2(), bankCountry.getCountryName(), bank.isHeadquarter(), bank.getSwiftCode(), null);
        }
        return convertHeadquarterToDTO(bank);
    }

    private BankDTO convertHeadquarterToDTO(Bank bank) {
        List<BranchDTO> branchesDTO = swiftCodesRepository.findBranchesByHeadquarter(bank.getSwiftCode().substring(0, 8))
                .stream()
                .map(this::convertBranchToDTO)
                .toList();
        Country bankCountry = bank.getCountry();
        return new BankDTO(bank.getAddress(), bank.getBankName(), bankCountry.getCountryISO2(), bankCountry.getCountryName(), bank.isHeadquarter(), bank.getSwiftCode(), branchesDTO);
    }

    private BranchDTO convertBranchToDTO(Bank bank) {
        Country bankCountry = bank.getCountry();
        return new BranchDTO(bank.getAddress(), bank.getBankName(), bankCountry.getCountryISO2(), bank.isHeadquarter(), bank.getSwiftCode());
    }


    private Bank convertToEntity(BranchDTO bankDTO, Country country) {
        return new Bank(bankDTO.swiftCode(), bankDTO.bankName(), bankDTO.address(), bankDTO.isHeadquarter(), country);
    }


    @Override
    public BanksFromCountryDTO getAllSwiftCodesFromCountryISO2(String countryISO2) {
        Country country = countriesRepository.findById(countryISO2).orElseThrow(() -> new CountryNotFoundException("Country with provided ISO2 code not found."));
        List<BranchDTO> banks = swiftCodesRepository.findByCountry_CountryISO2(countryISO2)
                .stream().
                map(this::convertBranchToDTO).
                toList();
        return new BanksFromCountryDTO(countryISO2, country.getCountryName(), banks);
    }

    @Override
    public void addSWIFTCode(BranchDTO bankDTO) {
        Country country = countriesRepository.findById(bankDTO.countryISO2()).orElseThrow(() -> new CountryNotFoundException("Country with provided ISO2 code not found."));
//        if (!country.getCountryName().equals(bankDTO.countryName())) {
//            return "Failure. Provided country ISO2 code does not match provided country name.";
//        }
        swiftCodesRepository.save(convertToEntity(bankDTO, country));
    }

    @Override
    public void deleteSWIFTCode(String SWIFTCode) {
        Bank bank = swiftCodesRepository.findById(SWIFTCode).orElseThrow(() -> new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."));
        swiftCodesRepository.deleteById(SWIFTCode);
    }
}
