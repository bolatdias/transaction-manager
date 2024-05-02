package com.example.demo.payload;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CurrencyDTO {
    private String symbol;
    private BigDecimal exchangeRate;
    private String baseSymbol;
    private OffsetDateTime exchangeDate;
}
