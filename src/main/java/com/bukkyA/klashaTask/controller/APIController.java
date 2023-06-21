package com.bukkyA.klashaTask.controller;

import com.bukkyA.klashaTask.payload.request.CurrencyConversionRequest;
import com.bukkyA.klashaTask.payload.response.CountryResponseInfo;
import com.bukkyA.klashaTask.payload.response.CurrencyConversionResponse;
import com.bukkyA.klashaTask.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class APIController {
    private final CountryService countryService;
    @GetMapping("topCities/{noOfCities}")
    public ResponseEntity<List<String>> getTopCities(@PathVariable int noOfCities) throws IOException {
        return ResponseEntity.ok(countryService.getTopCities(noOfCities));
    }
    @GetMapping("countryInfo/{country}")
    public ResponseEntity<CountryResponseInfo> getCountryInformation(@PathVariable String country) throws IOException {
        return ResponseEntity.ok(countryService.getCountryInformation(country));
    }
    @GetMapping("stateAndCities/{country}")
    public ResponseEntity<Map<String, List<String>>> getStateAndCities(@PathVariable String country) throws IOException {
        return ResponseEntity.ok(countryService.getStatesAndCities(country));
    }
    @GetMapping("conversion")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(@RequestBody CurrencyConversionRequest conversionRequest) throws IOException {
        return  ResponseEntity.ok(countryService.convertCurrency(conversionRequest));
    }
}
