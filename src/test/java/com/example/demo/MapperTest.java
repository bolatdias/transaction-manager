package com.example.demo;

import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MapperTest {

    CurrencyMapper currencyMapper = new CurrencyMapper();
    @Test
    public void testConversionDTOToCurrency() {
        // Arrange
        CurrencyConversionDTO currencyConversionDTO = new CurrencyConversionDTO();
        currencyConversionDTO.setSymbol("USD");
        currencyConversionDTO.setRate(BigDecimal.valueOf(1.0));

        // Act
        Currency currency = currencyMapper.conversionDTOToCurrency(currencyConversionDTO);

        // Assert
        assertEquals(currencyConversionDTO.getSymbol(), currency.getSymbol());
        assertEquals(currencyConversionDTO.getRate(), currency.getCloseExchange());
    }

}
