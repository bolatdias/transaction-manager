package com.example.demo.mapper;

import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class CurrencyMapper {
    public Currency conversionDTOToCurrency(CurrencyConversionDTO currencyDTO) {
        Currency currency = new Currency();
        currency.setSymbol(currencyDTO.getSymbol());
        currency.setExchangeDate(OffsetDateTime.now());
        currency.setCloseExchange(currencyDTO.getRate());
        return currency;
    }
}
