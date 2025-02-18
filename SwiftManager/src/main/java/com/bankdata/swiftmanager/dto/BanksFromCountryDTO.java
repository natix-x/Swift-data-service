package com.bankdata.swiftmanager.dto;

import java.util.List;

public record BanksFromCountryDTO(
        String countryISO2,
        String countryName,
        List<BranchDTO> swiftCodes
) {
}
