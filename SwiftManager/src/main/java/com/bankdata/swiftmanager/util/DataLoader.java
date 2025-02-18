package com.bankdata.swiftmanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final SwiftCodesCSVParser swiftCodesCSVParser;

    public DataLoader(SwiftCodesCSVParser swiftCodesCSVParser) {
        this.swiftCodesCSVParser = swiftCodesCSVParser;
    }

    @Override
    public void run(ApplicationArguments args)  {
        String filePath = "src/main/resources/swift_codes.csv";
        try {
            swiftCodesCSVParser.LoadToDatabase(filePath);
            System.out.println("Dane SWIFT zostały pomyślnie załadowane do bazy.");
        } catch (Exception e) {
            System.err.println("Błąd podczas ładowania danych SWIFT: " + e.getMessage());
        }
    }
    }
