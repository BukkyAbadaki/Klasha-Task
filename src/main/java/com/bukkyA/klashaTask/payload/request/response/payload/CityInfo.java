package com.bukkyA.klashaTask.payload.request.response.payload;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CityInfo {
    private String city;
    private String country;
    private PopulationCount[] populationCounts;
}
