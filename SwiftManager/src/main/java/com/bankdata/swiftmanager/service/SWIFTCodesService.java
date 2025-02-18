package com.bankdata.swiftmanager.service;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.model.Bank;

import java.util.List;

public interface SWIFTCodesService {
    public BankDTO getSWIFTCodeDetails(String SWIFTCode);
    public List<BranchDTO> getAllSWIFTCodesFromCountryISO2(String countryISO2);
    public void addSWIFTCode(BranchDTO bankDTO);
    public void deleteSWIFTCode(String SWIFTCode);
}
