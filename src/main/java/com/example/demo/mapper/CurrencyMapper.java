package com.example.demo.mapper;

import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
public class CurrencyMapper {
    public Currency conversionDTOToCurrency(CurrencyConversionDTO currencyDTO) {
        Currency currency = new Currency();
        currency.setSymbol(currencyDTO.getSymbol());
        currency.setExchangeDate(new Date(currencyDTO.getTimestamp()));
        currency.setCloseExchange(currencyDTO.getRate());
        return currency;
    }
}
