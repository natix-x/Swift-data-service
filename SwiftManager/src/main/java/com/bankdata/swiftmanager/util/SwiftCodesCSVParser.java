package com.bankdata.swiftmanager.util;

import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import com.bankdata.swiftmanager.repository.CountriesRepository;
import com.bankdata.swiftmanager.repository.SwiftCodesRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

@Component
public class SwiftCodesCSVParser {

    private final SwiftCodesRepository swiftCodesRepository;
    private final CountriesRepository countriesRepository;

    public SwiftCodesCSVParser(SwiftCodesRepository swiftCodesRepository, CountriesRepository countriesRepository) {
        this.swiftCodesRepository = swiftCodesRepository;
        this.countriesRepository = countriesRepository;
    }

    @Transactional
    public void LoadToDatabase(String filePath) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            csvReader.readNext();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {

                if (nextLine.length != 8) continue;
                processSingleRow(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error processing CSV", e);
        }
    }

    public void processSingleRow(String[] row) {
        String countryISO2 = row[0].trim().toUpperCase();
        String swiftCode = row[1].trim();
        String bankName = row[3].trim();
        String address = row[4].trim();
        if (address.isEmpty()) {
            address = row[5].trim() + ", " + row[6].trim();
        }
        String countryName = row[6].trim().toUpperCase();
        boolean isHeadquarters = swiftCode.endsWith("XXX");


        Country country = countriesRepository.findById(countryISO2).orElse(null);
        if (country == null) {
            country = addNewCountryToDatabase(countryName, countryISO2);
        }

        if (!swiftCodesRepository.existsById(swiftCode)) {
            addNewBankToDatabase(swiftCode, bankName, address, isHeadquarters, country);
        } else {
            System.out.println("Bank already exists: " + bankName);
        }
    }

    public Country addNewCountryToDatabase(String countryName, String countryISO2) {
        Country country = new Country();
        country.setCountryName(countryName);
        country.setCountryISO2(countryISO2);
        country.setBanks(new HashSet<>());
        countriesRepository.save(country);
        return country;
    }

    public void addNewBankToDatabase(String swiftCode, String bankName, String address, boolean isHeadquarters, Country country) {
        Bank bank = new Bank();
        bank.setSwiftCode(swiftCode);
        bank.setBankName(bankName);
        bank.setAddress(address);
        bank.setHeadquarter(isHeadquarters);
        bank.setCountry(country);
        country.getBanks().add(bank);
        swiftCodesRepository.save(bank);
    }

}

