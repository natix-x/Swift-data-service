package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.exception.SWIFTCodeNotFoundException;
import com.bankdata.swiftmanager.service.SWiftCodesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SwiftCodesController.class)
@ExtendWith(MockitoExtension.class)
public class SwiftCodesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SWiftCodesService swiftCodesService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Junit test for get details of existing SWIFT code.")
    public void givenExistingSwiftCode_whenGetSwiftCodeDetails_thenReturnResponse() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        BankDTO bankDTO = BankDTO.builder()
                        .address("Warszawa, Polska")
                                .bankName("Bank 1")
                                        .countryISO2("PL").countryName("Poland").isHeadquarter(false).swiftCode(swiftCode).build();
        given(swiftCodesService.getSWIFTCodeDetails(swiftCode)).willReturn(bankDTO);

        // when
        ResultActions response = mockMvc.perform(get("/v1/swift-codes/" + swiftCode));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.swiftCode", is(swiftCode)));
    }

    @Test
    @DisplayName("Junit test for get details of non-existing SWIFT code.")
    public void givenNonExistingSwiftCode_whenGetSwiftCodeDetails_thenReturnResponse() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        given(swiftCodesService.getSWIFTCodeDetails(swiftCode))
                .willThrow(new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."));

        // when
        ResultActions response = mockMvc.perform(get("/v1/swift-codes/" + swiftCode));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test for get all SWIFT codes from a country by ISO2 code.")
    public void givenCountryISO2_whenGetCountryBankCodes_thenReturnResponse() throws Exception {
        // given
        String countryISO2 = "PL";
        BranchDTO branchDTO = BranchDTO.builder()
                .address("Warszawa, Polska")
                .countryISO2(countryISO2)
                .isHeadquarter(false)
                .swiftCode("CFGYHJGS").build();

        BanksFromCountryDTO banksFromCountryDTO = BanksFromCountryDTO.builder()
                .countryISO2(countryISO2)
                .countryName("Poland")
                .swiftCodes(List.of(branchDTO))
                .build();
        given(swiftCodesService.getAllSwiftCodesFromCountryISO2(countryISO2)).willReturn(banksFromCountryDTO);

        // when
        ResultActions response = mockMvc.perform(get("/v1/swift-codes/country/" + countryISO2));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.countryISO2", is(countryISO2)))
                .andExpect(jsonPath("$.countryName", is("Poland")))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode", is("CFGYHJGS")));
    }

    //TODO: dodać test i implementację kodu jak kraj nieistniejący
    //TODO: dodać test jak dodajemy już istenijący swiftcode
    @Test
    @DisplayName("JUnit test for add a new SWIFT code")
    public void givenBranchDTO_whenAddBankCode_thenReturnResponse() throws Exception {
        // given
        BranchDTO branchDTO = BranchDTO.builder()
                .address("Warszawa, Polska")
                .bankName("Bank 1")
                .countryISO2("PL")
                .isHeadquarter(false)
                .swiftCode("CFGYHJGS").build();
        willDoNothing().given(swiftCodesService).addSWIFTCode(any(BranchDTO.class));
        // when
        ResultActions response = mockMvc.perform(post("/v1/swift-codes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchDTO)));

        // then
        response.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("JUnit test for delete existing SWIFT code")
    public void givenExistingSwiftCode_whenDeleteBankCode_thenReturnResponse() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        willDoNothing().given(swiftCodesService).deleteSWIFTCode(swiftCode);
        // when
        ResultActions response = mockMvc.perform(delete("/v1/swift-codes/" + swiftCode));
        // then
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test for delete non-existing SWIFT code")
    public void givenNonExistingSwiftCode_whenDeleteBankCode_thenReturnNotFound() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";

        doThrow(new SWIFTCodeNotFoundException("Bank with provided SWIFT code not found."))
                .when(swiftCodesService).deleteSWIFTCode(swiftCode);

        // when
        ResultActions response = mockMvc.perform(delete("/v1/swift-codes/" + swiftCode));

        // then
        response.andExpect(status().isNotFound());
    }
}
