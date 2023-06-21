package com.bukkyA.klashaTask.payload.request;

import java.math.BigDecimal;

public record CurrencyConversionRequest(String country, BigDecimal amount, String targetCurrency) {
}
