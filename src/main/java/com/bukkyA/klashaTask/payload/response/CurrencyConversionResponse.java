package com.bukkyA.klashaTask.payload.response;

import java.math.BigDecimal;

public record CurrencyConversionResponse(String currency, BigDecimal targetAmount) {
}
