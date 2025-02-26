package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.response.ApiResponse;
import com.bankdata.swiftmanager.response.ResponseUtil;
import com.bankdata.swiftmanager.service.SWiftCodesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes/")
public class SwiftCodesController {

    SWiftCodesService SWIFTCodesService;

    public SwiftCodesController(SWiftCodesService SWIFTCodesService) {
        this.SWIFTCodesService = SWIFTCodesService;
    }

    @GetMapping("/{swift-code}")
    public ResponseEntity<BankDTO> getSwiftCodeDetails(@PathVariable("swift-code") String swiftCode)
    {
        BankDTO bankDetails = SWIFTCodesService.getSWIFTCodeDetails(swiftCode);
        return ResponseEntity.ok(bankDetails);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<BanksFromCountryDTO> getCountryBankCodes(@PathVariable("countryISO2code") String countryISO2)
    {
        BanksFromCountryDTO banks = SWIFTCodesService.getAllSwiftCodesFromCountryISO2(countryISO2);
        return ResponseEntity.ok(banks);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> addBankCode(@RequestBody BranchDTO bankDTO) {
        SWIFTCodesService.addSWIFTCode(bankDTO);
        return ResponseEntity.ok(ResponseUtil.successMessageDisplay("New SWIFT code data added successfully."));
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<ApiResponse<String>> deleteBankCode(@PathVariable("swift-code") String swiftCode) {
        SWIFTCodesService.deleteSWIFTCode(swiftCode);
        return ResponseEntity.ok(ResponseUtil.successMessageDisplay("Swift code data deleted successfully."));
    }
}
