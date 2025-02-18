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

                String countryISO2 = nextLine[0].trim().toUpperCase();
                String swiftCode = nextLine[1].trim();
                String bankName = nextLine[3].trim();
                String address = nextLine[4].trim();
                if (address.isEmpty()) {
                    address = nextLine[5].trim() + nextLine[6].trim();
                }
                String countryName = nextLine[6].trim().toUpperCase();
                boolean isHeadquarters = swiftCode.endsWith("XXX");


                Country country = countriesRepository.findById(countryISO2).orElse(null);
                if (country == null) {
                    country = new Country();
                    country.setCountryName(countryName);
                    country.setCountryISO2(countryISO2);
                    country.setBanks(new HashSet<>());
                    countriesRepository.save(country);
                }

                Bank bank = swiftCodesRepository.findById(swiftCode).orElse(null);
                if (bank == null) {
                    bank = new Bank();
                    bank.setSwiftCode(swiftCode);
                    bank.setBankName(bankName);
                    bank.setAddress(address);
                    bank.setHeadquarter(isHeadquarters);
                    bank.setCountry(country);
                    country.getBanks().add(bank);
                    swiftCodesRepository.save(bank);
                    countriesRepository.save(country);
                } else {
                    System.out.println("Bank already exists: " + bankName);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing CSV", e);
        }
    }
}

