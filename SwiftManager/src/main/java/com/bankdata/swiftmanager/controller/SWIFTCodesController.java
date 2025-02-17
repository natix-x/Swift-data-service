package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.service.SWIFTCodesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes/")
public class SWIFTCodesController {

    SWIFTCodesService SWIFTCodesService;

    public SWIFTCodesController(com.bankdata.swiftmanager.service.SWIFTCodesService SWIFTCodesService) {
        this.SWIFTCodesService = SWIFTCodesService;
    }

    @GetMapping("/{swift-code}")
    public BankDTO getSwiftCodeDetails(@PathVariable("swift-code") String SWIFTCode)
    {
        return SWIFTCodesService.getSWIFTCodeDetails(SWIFTCode);
    }

    @GetMapping("/country/{countryISO2code}")
    public List<BranchDTO> getCountryBankCodes(@PathVariable("countryISO2code") String countryISO2)
    {
        return SWIFTCodesService.getAllSWIFTCodesFromCountryISO2(countryISO2);
    }

    @PostMapping
    public String addBankCode(@RequestBody BranchDTO bankDTO) {
        System.out.println("Received DTO: " + bankDTO);
        return SWIFTCodesService.addSWIFTCode(bankDTO);
    }

    @DeleteMapping("/{swift-code}")
    public String deleteBankCode(@PathVariable("swift-code") String SWIFTCode) {
        return SWIFTCodesService.deleteSWIFTCode(SWIFTCode);
    }

}
