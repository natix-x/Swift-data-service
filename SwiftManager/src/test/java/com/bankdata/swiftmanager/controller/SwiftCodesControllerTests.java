package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.dto.BankDTO;
import com.bankdata.swiftmanager.dto.BanksFromCountryDTO;
import com.bankdata.swiftmanager.dto.BranchDTO;
import com.bankdata.swiftmanager.exception.CountryNotFoundException;
import com.bankdata.swiftmanager.exception.SwiftCodeNotFoundException;
import com.bankdata.swiftmanager.exception.SwiftCodeAlreadyExistsException;
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
import static org.mockito.BDDMockito.*;
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
    @DisplayName("Unit test for get details of existing SWIFT code.")
    public void givenExistingSwiftCode_whenGetSwiftCodeDetails_thenReturnSuccessResponse() throws Exception {
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
    @DisplayName("Unit test for get details of non-existing SWIFT code.")
    public void givenNonExistingSwiftCode_whenGetSwiftCodeDetails_thenReturnErrorResponse() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        given(swiftCodesService.getSWIFTCodeDetails(swiftCode))
                .willThrow(new SwiftCodeNotFoundException("Bank with provided SWIFT code not found."));

        // when
        ResultActions response = mockMvc.perform(get("/v1/swift-codes/" + swiftCode));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Resource not found")));
    }

    @Test
    @DisplayName("Unit test for get all SWIFT codes from a country by ISO2 code.")
    public void givenCountryISO2_whenGetCountryBankCodes_thenReturnSuccessResponse() throws Exception {
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


    @Test
    @DisplayName("Unit test for add SWIFT code associated with non-existing country.")
    public void givenBranchDTOWithExistingSwiftCode_whenAddBankCode_thenReturnCountryNotFound() throws Exception {
        // given
        String nonExistingISO2 = "XX";

        given(swiftCodesService.getAllSwiftCodesFromCountryISO2("XX")).willThrow(new CountryNotFoundException("Country with provided ISO2 code not found."));

        // when
        ResultActions response = mockMvc.perform(get("/v1/swift-codes/country/" + nonExistingISO2));
        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is("Country with provided ISO2 code not found.")))
                .andExpect(jsonPath("$.message", is("Resource not found")));
    }


    @Test
    @DisplayName("Unit test for add already existing SWIFT code.")
    public void givenBranchDTOWithAlreadyExistingSwiftCode_whenAddBankCode_thenReturnSwiftCodeAlreadyExist() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        BranchDTO branchDTO = BranchDTO.builder()
                .address("Warszawa, Polska")
                .bankName("Bank 1")
                .countryISO2("PL")
                .isHeadquarter(false)
                .swiftCode(swiftCode).build();
        willThrow(new SwiftCodeAlreadyExistsException("SWIFT code already exists.")).given(swiftCodesService).addSWIFTCode(branchDTO);

        // when
        ResultActions response = mockMvc.perform(post("/v1/swift-codes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchDTO)));
        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is("SWIFT code already exists.")));
    }

    @Test
    @DisplayName("Unit test for add a new SWIFT code")
    public void givenBranchDTO_whenAddBankCode_thenReturnSuccessResponse() throws Exception {
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
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("New SWIFT code data added successfully.")));
    }

    @Test
    @DisplayName("Unit test for delete existing SWIFT code")
    public void givenExistingSwiftCode_whenDeleteBankCode_thenReturnResponse() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";
        willDoNothing().given(swiftCodesService).deleteSWIFTCode(swiftCode);
        // when
        ResultActions response = mockMvc.perform(delete("/v1/swift-codes/" + swiftCode));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Swift code data deleted successfully.")));;
    }

    @Test
    @DisplayName("Unit test for delete non-existing SWIFT code")
    public void givenNonExistingSwiftCode_whenDeleteBankCode_thenReturnNotFound() throws Exception {
        // given
        String swiftCode = "CFGYHJGS";

        doThrow(new SwiftCodeNotFoundException("Bank with provided SWIFT code not found."))
                .when(swiftCodesService).deleteSWIFTCode(swiftCode);

        // when
        ResultActions response = mockMvc.perform(delete("/v1/swift-codes/" + swiftCode));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Resource not found")));
    }
}
