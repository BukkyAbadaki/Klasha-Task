package com.bukkyA.klashaTask.payload.request.response;

import com.bukkyA.klashaTask.payload.request.response.payload.Country;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StateRequestResponse extends RequestResponse{
    private Country data;
}
