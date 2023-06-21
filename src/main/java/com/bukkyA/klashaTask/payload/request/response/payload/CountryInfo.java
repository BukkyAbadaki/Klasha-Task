package com.bukkyA.klashaTask.payload.request.response.payload;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountryInfo {
    private String country;
    private String code;
    private String iso3;
    private PopulationCount[] populationCounts;
}
