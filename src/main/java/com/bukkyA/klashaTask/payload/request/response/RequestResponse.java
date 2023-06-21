package com.bukkyA.klashaTask.payload.request.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RequestResponse {
    boolean error;
    String msg;
}
