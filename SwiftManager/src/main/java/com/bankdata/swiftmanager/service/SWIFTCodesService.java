package com.bankdata.swiftmanager.service;

public interface SWIFTCodesService {
    public String getSWIFTCodeDetails(String SWIFTCode);
    public String getAllSWIFTCodesFromCountryISO2Code(String countryISO2);
    public String addSWIFTCode(String SWIFTCode, String bankName, String address, String countryISO2, String countryName, boolean isHeadquarter);
    public String deleteSWIFTCode(String SWIFTCode);
}
