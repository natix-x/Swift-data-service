package com.bankdata.swiftmanager.controller;

import com.bankdata.swiftmanager.model.Bank;
import com.bankdata.swiftmanager.model.Country;
import com.bankdata.swiftmanager.repository.CountriesRepository;
import com.bankdata.swiftmanager.repository.SwiftCodesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class SwiftCodesControllerIntegrationTests {

    @Container
    public static final MySQLContainer MY_SQL_CONTAINER;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodesRepository swiftCodesRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

    }

    @BeforeEach
    public void beforeEach() {
        Country country = Country.builder()
                .countryISO2("PL")
                .countryName("Poland")
                .build();
        countriesRepository.save(country);

        Bank bank = Bank.builder()
                .swiftCode("FGTHJKDS")
                .bankName("Bank 1")
                .address("Warszawa, Mazowickie")
                .country(country)
                .isHeadquarter(false)
                .build();
        swiftCodesRepository.save(bank);
    }

    @AfterEach
    public void afterEach() {
        swiftCodesRepository.deleteAll();
        countriesRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration test for get details of existing SWIFT code.")
    public void givenExistingSwiftCode_whenGetSwiftCodeDetails_returnSwiftCodeDetails() throws Exception {
        String swiftCode = "FGTHJKDS";
        mockMvc.perform(get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode", is(swiftCode)));
    }

    @Test
    @DisplayName("Integration test for get details of non-existing SWIFT code.")
    public void givenNonExistingSwiftCode_whenGetSwiftCodeDetails_returnErrorMessage() throws Exception {
        String swiftCode = "AAAAA";
        mockMvc.perform(get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Resource not found")));
    }
}