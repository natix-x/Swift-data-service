package com.bankdata.swiftmanager.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class DataLoader implements ApplicationRunner {

    private final SwiftCodesCSVParser swiftCodesCSVParser;

    public DataLoader(SwiftCodesCSVParser swiftCodesCSVParser) {
        this.swiftCodesCSVParser = swiftCodesCSVParser;
    }

    @Override
    public void run(ApplicationArguments args)  {
        String filePath = "/data/swift_codes.csv";
        try {
            swiftCodesCSVParser.LoadToDatabase(filePath);
            System.out.println("Swift data successfully loaded");
        } catch (Exception e) {
            System.err.println("Error occurred during data loading: " + e.getMessage() + e.getCause());
        }
    }
    }
