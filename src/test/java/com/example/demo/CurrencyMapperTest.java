package com.example.demo;

import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyMapperTest {

    @Test
    public void testConversionDTOToCurrency() {

        CurrencyConversionDTO currencyDTO = new CurrencyConversionDTO();
        currencyDTO.setSymbol("USD");
        currencyDTO.setRate(BigDecimal.valueOf(1.2));


        Currency currency = CurrencyMapper.INSTANCE.conversionDTOToCurrency(currencyDTO);


        assertEquals(currencyDTO.getSymbol(), currency.getSymbol());
        assertEquals(currencyDTO.getRate(), currency.getCloseExchange());
        assertEquals(OffsetDateTime.now().getDayOfYear(), currency.getExchangeDate().getDayOfYear()); // You can adjust this comparison as per your needs
    }
}
