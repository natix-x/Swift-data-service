package com.bankdata.swiftmanager.dto;

public record BranchDTO (String address,
                         String bankName,
                         String countryISO2,
                         boolean isHeadquarter,
                         String swiftCode) { }