package com.bukkyA.klashaTask.payload.request.response;

import com.bukkyA.klashaTask.payload.request.response.payload.CityInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TopCitiesRequestResponse extends RequestResponse{
    CityInfo[] data;
}
