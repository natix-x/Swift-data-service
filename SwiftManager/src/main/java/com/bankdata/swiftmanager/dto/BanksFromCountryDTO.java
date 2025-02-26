package com.bankdata.swiftmanager.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BanksFromCountryDTO(
        String countryISO2,
        String countryName,
        List<BranchDTO> swiftCodes
) {
}
