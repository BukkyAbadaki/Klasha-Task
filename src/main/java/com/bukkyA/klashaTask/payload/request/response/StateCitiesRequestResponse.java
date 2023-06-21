package com.bukkyA.klashaTask.payload.request.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StateCitiesRequestResponse extends RequestResponse {
    private String[] data;
}
