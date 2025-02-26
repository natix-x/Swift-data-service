package com.bankdata.swiftmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BankDTO(String address,
                      String bankName,
                      String countryISO2,
                      String countryName,
                      boolean isHeadquarter,
                      String swiftCode,
                      List<BranchDTO> branches) {
}
