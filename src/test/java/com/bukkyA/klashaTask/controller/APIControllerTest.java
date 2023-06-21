package com.bukkyA.klashaTask.controller;

import com.bukkyA.klashaTask.payload.request.CurrencyConversionRequest;
import com.bukkyA.klashaTask.payload.response.CountryResponseInfo;
import com.bukkyA.klashaTask.payload.response.CurrencyConversionResponse;
import com.bukkyA.klashaTask.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class APIControllerTest {
    @MockBean
    private CountryService countryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testGetTopCities() throws Exception {
        // Mocking the service response
        List<String> cities =  cities = Arrays.asList("Zalanga", "Zadawa", "Yuli");
        when(countryService.getTopCities(anyInt())).thenReturn(cities);

        // Perform the GET request

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/topCities/{noOfCities}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("Zalanga"))
                .andExpect(jsonPath("$[1]").value("Zadawa"));

        // Verify that the service method was called with the correct argument
        verify(countryService).getTopCities(2);
    }
    @Test
    public void testGetCountryInformation() throws Exception {
        // Mocking the service response
        CountryResponseInfo countryResponse =
                new CountryResponseInfo("195874740", "Abuja", "8 , 10 ", "NGN", "NG&NGA");
        when(countryService.getCountryInformation(anyString())).thenReturn(countryResponse);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/countryInfo/{country}", "Nigeria")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.population").value("195874740"))
                .andExpect(jsonPath("$.capitalCity").value("Abuja"))
                .andExpect(jsonPath("$.location").value("8 , 10 "))
                .andExpect(jsonPath("$.currency").value("NGN"))
                .andExpect(jsonPath("$.ISO2_3").value("NG&NGA"));

        // Verify that the service method was called with the correct argument
        verify(countryService).getCountryInformation("Nigeria");
    }

    @Test
    void getStateAndCities() {
    }

    @Test
    public void testConvertCurrency() throws Exception {
        // Mocking the service response
        CurrencyConversionResponse conversionResponse = new CurrencyConversionResponse("NGN", BigDecimal.valueOf(142));
        CurrencyConversionRequest conversionRequest =
                new CurrencyConversionRequest("Nigeria",BigDecimal.valueOf(70000),"EUR");
        when(countryService.convertCurrency(conversionRequest)).thenReturn(conversionResponse);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/conversion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversionRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("NGN"))
                .andExpect(jsonPath("$.targetAmount").value(142));

        // Verify that the service method was called with the correct argument
        verify(countryService).convertCurrency(conversionRequest);
    }
    @Test
    public void testGetStateAndCities() throws Exception {
        // Mocking the service response
        Map<String, List<String>> stateAndCities = new HashMap<>();
        stateAndCities.put("Kaduna State", Arrays.asList("Anchau", "Burumburum", "Dutsen Wai", "Hunkuyi", "Kachia"));
        stateAndCities.put("Enugu State", Arrays.asList("Adani", "Aku","Enugu"));
        when(countryService.getStatesAndCities(anyString())).thenReturn(stateAndCities);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stateAndCities/{country}", "Nigeria")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['Kaduna State']").isArray())
                .andExpect(jsonPath("['Kaduna State'][0]").value("Anchau"))
                .andExpect(jsonPath("['Kaduna State'][1]").value("Burumburum"))
                .andExpect(jsonPath("['Enugu State']").isArray())
                .andExpect(jsonPath("['Enugu State'][0]").value("Adani"))
                .andExpect(jsonPath("['Enugu State'][1]").value("Aku"));

        // Verify that the service method was called with the correct argument
        verify(countryService).getStatesAndCities("Nigeria");
    }
}