package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.response.ApiResponse;
import com.bankdata.swiftmanager.response.ResponseUtil;
import com.bankdata.swiftmanager.service.SWIFTCodesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BankDTO> getSwiftCodeDetails(@PathVariable("swift-code") String SWIFTCode)
    {
        BankDTO bankDetails = SWIFTCodesService.getSWIFTCodeDetails(SWIFTCode);
        return ResponseEntity.ok(bankDetails);
    }

    // TODO popraw format zwracany
    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<List<BranchDTO>> getCountryBankCodes(@PathVariable("countryISO2code") String countryISO2)
    {
        List<BranchDTO> banksInCountry = SWIFTCodesService.getAllSWIFTCodesFromCountryISO2(countryISO2);
        return ResponseEntity.ok(banksInCountry);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> addBankCode(@RequestBody BranchDTO bankDTO) {
        SWIFTCodesService.addSWIFTCode(bankDTO);
        return ResponseEntity.ok(ResponseUtil.successMessageDisplay("New SWIFT code data added successfully."));
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<ApiResponse<String>> deleteBankCode(@PathVariable("swift-code") String SWIFTCode) {
        SWIFTCodesService.deleteSWIFTCode(SWIFTCode);
        return ResponseEntity.ok(ResponseUtil.successMessageDisplay("Swift code data deleted successfully."));
    }
}
