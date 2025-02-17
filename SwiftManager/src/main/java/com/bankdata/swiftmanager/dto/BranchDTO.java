package com.bankdata.swiftmanager.dto;

public record BranchDTO (String SWIFTCode, String bankName,
                        String address, boolean isHeadquarter, String countryName, String countryISO2) { }
