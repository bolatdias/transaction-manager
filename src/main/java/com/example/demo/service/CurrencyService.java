package com.example.demo.service;


import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.proxy.CurrencyProxy;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.utils.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    @Value("${app.service.apiKey}")
    private String apiKey;

    private final CurrencyProxy currencyProxy;
    private final CurrencyRepository currencyRepository;


    public void parseCurrencyApi(List<String> parseStrings) {
        HashMap<String, Currency> currHashMap = initHashMap();


        for (String parseString : parseStrings) {
            String symbol = AppConst.BASE_CURRENCY + "/" + parseString;
            CurrencyConversionDTO currencyDTO = getExchangeRateApi(symbol);


            insertCurrency(currHashMap, parseString, currencyDTO);
        }
    }

    public void insertCurrency(HashMap<String, Currency> currHashMap, String symbol, CurrencyConversionDTO currencyDTO) {

        Currency currency;

        if (currHashMap.containsKey(symbol)) {
            currency = currHashMap.get(symbol);
            currency.setPreviousCloseExchange(currency.getCloseExchange());
            currency.setCloseExchange(currency.getCloseExchange());
            currency.setExchangeDate(OffsetDateTime.now());

        } else {
            currency = CurrencyMapper.INSTANCE.conversionDTOToCurrency(currencyDTO);
            currency.setSymbol(symbol);
        }
        currencyRepository.save(currency);

    }


    private HashMap<String, Currency> initHashMap() {
        HashMap<String, Currency> currenciesWithDate = new HashMap<>();
        List<Currency> currencyList = currencyRepository.findAll();
        for (Currency currency : currencyList) {
            currenciesWithDate.put(currency.getSymbol(), currency);
        }
        return currenciesWithDate;
    }

    private CurrencyConversionDTO getExchangeRateApi(String symbol) {
        return currencyProxy.getExchangeRate(apiKey, symbol);
    }


    public Currency getCurrencyBySymbol(String s) {
        Currency currency = currencyRepository.findBySymbol(s).orElseThrow(
                () -> new RuntimeException("Currency not found")
        );

        return currency;
    }

}

