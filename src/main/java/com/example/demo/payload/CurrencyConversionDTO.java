package com.example.demo.payload;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyConversionDTO {
    private String symbol;
    private BigDecimal rate;
    private long timestamp;
}
