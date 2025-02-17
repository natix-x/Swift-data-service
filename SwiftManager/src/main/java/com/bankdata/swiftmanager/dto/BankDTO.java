package com.bankdata.swiftmanager.dto;

import java.util.List;

public record BankDTO(String SWIFTCode, String bankName,
                      String address, boolean isHeadquarter, List<BranchDTO> branches, String countryName, String countryISO2) {
}
