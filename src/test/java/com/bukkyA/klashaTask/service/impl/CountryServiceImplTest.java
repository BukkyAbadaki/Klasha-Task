package com.bukkyA.klashaTask.service.impl;

import com.bukkyA.klashaTask.payload.request.CurrencyConversionRequest;
import com.bukkyA.klashaTask.payload.request.response.CapitalRequestResponse;
import com.bukkyA.klashaTask.payload.request.response.TopCitiesRequestResponse;
import com.bukkyA.klashaTask.payload.request.response.payload.CityInfo;
import com.bukkyA.klashaTask.payload.response.CountryResponseInfo;
import com.bukkyA.klashaTask.payload.response.CurrencyConversionResponse;
import com.bukkyA.klashaTask.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
class CountryServiceImplTest {
    @Mock
    private CloseableHttpClient httpClient;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetTopCities() throws IOException {
        // Mocking the HTTP client and response
        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = Mockito.mock(HttpEntity.class);
        when(httpClient.execute(Mockito.any())).thenReturn(response);
        when(response.getEntity()).thenReturn(httpEntity);

        // Mocking the ObjectMapper and JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        TopCitiesRequestResponse topCitiesRequestResponse = new TopCitiesRequestResponse();
        // Set up the top cities

        String jsonResponse = objectMapper.writeValueAsString(topCitiesRequestResponse);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));


        // Perform the test
        List<String> topCities = countryService.getTopCities(2);

        // Verify the results

        Assertions.assertEquals(new ArrayList(List.of("Tema","Tauranga")),topCities);
        Assertions.assertEquals(2,topCities.size());
    }

    @Test
    public void testGetCountryInformation() throws IOException {
        // Mocking the HTTP client and response
        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = Mockito.mock(HttpEntity.class);
        when(httpClient.execute(Mockito.any())).thenReturn(response);
        when(response.getEntity()).thenReturn(httpEntity);

        // Mocking the ObjectMapper and JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        CapitalRequestResponse capitalRequestResponse = new CapitalRequestResponse();
        // Set up the capital

        String jsonResponse = objectMapper.writeValueAsString(capitalRequestResponse);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));

        // Perform the test
        CountryResponseInfo countryInfo = countryService.getCountryInformation("Nigeria");

        // Verify the results

        Assertions.assertEquals("Abuja",countryInfo.capitalCity());
        Assertions.assertEquals("195874740",countryInfo.population());
        Assertions.assertEquals("8 , 10 ",countryInfo.location());
        Assertions.assertEquals("NGN",countryInfo.currency());
        Assertions.assertEquals("NG&NGA",countryInfo.ISO2_3());
    }

    @Test
    public void testGetStatesAndCities() throws IOException {
        String countryName = "Nigeria"; // Replace with the desired country name

        Map<String, List<String>> statesAndCities = countryService.getStatesAndCities(countryName);

        assertNotNull(statesAndCities);
        assertFalse(statesAndCities.isEmpty());

        // Assert specific states and their associated cities
        List<String> citiesInState1 = statesAndCities.get("Lagos State");
        assertNotNull(citiesInState1);
        assertFalse(citiesInState1.isEmpty());

        List<String> citiesInState2 = statesAndCities.get("Ogun State");
        assertNotNull(citiesInState2);
        assertFalse(citiesInState2.isEmpty());
    }
    @Test
    public void testConvertCurrency() throws IOException {

        CurrencyConversionRequest conversionRequest = new CurrencyConversionRequest("Nigeria", BigDecimal.valueOf(70000),"EUR");

        CurrencyConversionResponse conversionResponse = countryService.convertCurrency(conversionRequest);

        assertNotNull(conversionResponse);
        assertEquals("NGN", conversionResponse.currency());
        assertEquals(BigDecimal.valueOf(142), conversionResponse.targetAmount());
    }

}