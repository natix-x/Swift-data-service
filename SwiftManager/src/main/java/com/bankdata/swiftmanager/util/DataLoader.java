package com.bankdata.swiftmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final SwiftCodesCSVParser swiftCodesCSVParser;

    public DataLoader(SwiftCodesCSVParser swiftCodesCSVParser) {
        this.swiftCodesCSVParser = swiftCodesCSVParser;
    }

    @Override
    public void run(ApplicationArguments args)  {
        String filePath = "/data/swift_codes.csv";
        try {
            swiftCodesCSVParser.LoadToDatabase(filePath);
            logger.info("Swift data successfully loaded");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    }
