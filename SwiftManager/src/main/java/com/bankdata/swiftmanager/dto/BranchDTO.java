package com.bankdata.swiftmanager.dto;

import lombok.Builder;

@Builder
public record BranchDTO (String address,
                         String bankName,
                         String countryISO2,
                         boolean isHeadquarter,
                         String swiftCode) { }