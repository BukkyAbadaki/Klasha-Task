package com.bukkyA.klashaTask.service;

import com.bukkyA.klashaTask.payload.request.CurrencyConversionRequest;
import com.bukkyA.klashaTask.payload.response.CountryResponseInfo;
import com.bukkyA.klashaTask.payload.response.CurrencyConversionResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CountryService {
    List<String> getTopCities(int amount) throws IOException;
    CountryResponseInfo getCountryInformation(String countryName) throws IOException;
    Map<String, List<String>> getStatesAndCities(String countryName) throws IOException;
    CurrencyConversionResponse convertCurrency(CurrencyConversionRequest conversionRequest) throws IOException;
}
