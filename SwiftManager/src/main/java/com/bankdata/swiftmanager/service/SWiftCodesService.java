package com.bankdata.swiftmanager.service;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;

public interface SWiftCodesService {
    public BankDTO getSWIFTCodeDetails(String SWIFTCode);
    public BanksFromCountryDTO getAllSwiftCodesFromCountryISO2(String countryISO2);
    public void addSWIFTCode(BranchDTO bankDTO);
    public void deleteSWIFTCode(String SWIFTCode);
}
