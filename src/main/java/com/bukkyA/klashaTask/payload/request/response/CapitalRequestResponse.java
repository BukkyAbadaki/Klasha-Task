package com.bukkyA.klashaTask.payload.request.response;

import com.bukkyA.klashaTask.payload.request.response.payload.CapitalInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CapitalRequestResponse extends  RequestResponse{
    private CapitalInfo data;
}
