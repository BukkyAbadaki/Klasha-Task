package com.bukkyA.klashaTask.payload.request.response;

import com.bukkyA.klashaTask.payload.request.response.payload.CountryInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountryPopulationRequestResponse extends RequestResponse{
    private CountryInfo data;
}
