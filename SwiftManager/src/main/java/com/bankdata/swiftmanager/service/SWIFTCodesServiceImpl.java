package com.bankdata.swiftmanager.service;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.exception.CountryNotFoundException;
import com.bankdata.swiftmanager.exception.SWIFTCodeNotFoundException;
import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import com.bankdata.swiftmanager.repository.CountriesRepository;
import com.bankdata.swiftmanager.repository.SWIFTCodesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SWIFTCodesServiceImpl implements SWIFTCodesService {

    private SWIFTCodesRepository SWIFTCodesRepository;
    private CountriesRepository CountriesRepository;

    public SWIFTCodesServiceImpl(SWIFTCodesRepository SWIFTCodesRepository) {
    }

    // TODO: eliminacja powtarzającego się kodu
    @Override
    public BankDTO getSWIFTCodeDetails(String SWIFTCode) {
        Bank bank = SWIFTCodesRepository.findById(SWIFTCode).orElseThrow(()-> new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."));
        if (!bank.isHeadquarter()) {
            Country bankCountry = bank.getCountry();
            return new BankDTO(bank.getSWIFTCode(), bank.getBankName(), bank.getAddress(), bank.isHeadquarter(), null, bankCountry.getCountryName(), bankCountry.getCountryISO2());
        }
        return convertHeadquarterToDTO(bank);
    }

    private BankDTO convertHeadquarterToDTO(Bank bank) {
        List<BranchDTO> branchesDTO = SWIFTCodesRepository.findBranchesByHeadquarter(bank.getSWIFTCode().substring(0,8))
                .stream()
                .map(this::convertBranchToDTO)
                .toList();
        Country bankCountry = bank.getCountry();
        return new BankDTO(bank.getSWIFTCode(), bank.getBankName(), bank.getAddress(), bank.isHeadquarter(), branchesDTO, bankCountry.getCountryName(), bankCountry.getCountryISO2());
    }

    private BranchDTO convertBranchToDTO(Bank bank) {
        Country bankCountry = bank.getCountry();
        return new BranchDTO(bank.getSWIFTCode(), bank.getBankName(), bank.getAddress(), bank.isHeadquarter(), bankCountry.getCountryName(), bankCountry.getCountryISO2());
    }


    private Bank convertToEntity(BankDTO bankDTO, Country country) {
        return new Bank(bankDTO.SWIFTCode(), bankDTO.bankName(), bankDTO.address(), bankDTO.isHeadquarter(), country);
    }


    @Override
    public List<BranchDTO> getAllSWIFTCodesFromCountryISO2(String countryISO2) {
        return SWIFTCodesRepository.findByCountry_CountryISO2(countryISO2).stream().map(this::convertBranchToDTO).toList();
    }

    // TODO implement
    @Override
    public String addSWIFTCode(BankDTO bankDTO) {
        Country country = CountriesRepository.findById(bankDTO.countryISO2()).orElseThrow(() -> new CountryNotFoundException("Country with provided ISO2 code not found."));
        if (!country.getCountryName().equals(bankDTO.countryName())) {
            return "Failure. Provided country ISO2 code does not match provided country name.";
        }
        SWIFTCodesRepository.save(convertToEntity(bankDTO, country));
        return "Success";
    }

    @Override
    public String deleteSWIFTCode(String SWIFTCode) {
        Bank bank = SWIFTCodesRepository.findById(SWIFTCode).orElseThrow(() -> new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."));
        SWIFTCodesRepository.deleteById(SWIFTCode);
        return "Bank with provided SWIFT code deleted successfully.";
    }
}
