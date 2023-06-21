package com.bukkyA.klashaTask.payload.request.response.payload;

public record Country(String name, String iso3, String iso2, State[] states) {
}
