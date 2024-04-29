package com.example.demo.service;


import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.payload.CurrencyConversionInputDTO;
import com.example.demo.proxy.CurrencyProxy;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.utils.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private static final Logger logger = Logger.getLogger(CurrencyService.class.getName());
    @Value("${app.service.apiKey}")
    private String apiKey;

    private final CurrencyProxy currencyProxy;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;


    public CurrencyConversionDTO getExchangeRate(CurrencyConversionInputDTO input) {
        CurrencyConversionDTO currencyConversionDTO = currencyProxy.getExchangeRate(apiKey, input.getSymbol());
        Currency currency = currencyMapper.conversionDTOToCurrency(currencyConversionDTO);
//        currencyRepository.save(currency);

        return currencyConversionDTO;
    }

    public void parseCurrency() {
        HashMap<String, Currency> currenciesWithDate = new HashMap<>();
        List<Currency> currencyList = currencyRepository.findAll();
        for (Currency currency : currencyList) {
            currenciesWithDate.put(currency.getSymbol(), currency);
        }


        List<String> parseCurrencyStrings = AppConst.OTHER_CURRENCY;
        for (String parseCurrencyString : parseCurrencyStrings) {
            String symbol = parseCurrencyString + "/" + AppConst.BASE_CURRENCY;
            CurrencyConversionDTO currencyDTO = getExchangeRate(symbol);


            if (currenciesWithDate.containsKey(symbol)) {
                Currency currency = currenciesWithDate.get(symbol);
                currency.setPreviousCloseExchange(currency.getCloseExchange());
                currency.setCloseExchange(currency.getCloseExchange());
                currency.setExchangeDate(new Date());
                currencyRepository.save(currency);

            } else {
                Currency currency = new Currency();
                currency.setSymbol(currencyDTO.getSymbol());
                currency.setExchangeDate(new Date());
                currency.setCloseExchange(currencyDTO.getRate());
                currency.setPreviousCloseExchange(currencyDTO.getRate());
                currencyRepository.save(currency);
            }

        }
    }

    private CurrencyConversionDTO getExchangeRate(String symbol) {
        return currencyProxy.getExchangeRate(apiKey, symbol);
    }

}
