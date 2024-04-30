package com.example.demo;

import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.proxy.CurrencyProxy;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.PrepareTestInstance;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyProxy currencyProxy;

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void testGetCurrencyBySymbol() {
        String symbol = "KZT";
        Currency currency = new Currency();
        currency.setSymbol(symbol);
        currency.setCloseExchange(new BigDecimal("442")); // Set the expected exchange rate


        when(currencyRepository.findBySymbol(symbol)).thenReturn(Optional.of(currency));
        Currency currency2 = currencyService.getCurrencyBySymbol(symbol);
        assertEquals(currency.getSymbol(), currency2.getSymbol()); // Asserting against the expected exchange rate
    }

    @Test
    public void testParseCurrencyApi() {
        // Set up mockCurrencyDTO
        CurrencyConversionDTO mockCurrencyDTO = new CurrencyConversionDTO();
        mockCurrencyDTO.setRate(BigDecimal.valueOf(442)); // Set exchange rate as per your need
        mockCurrencyDTO.setSymbol("USD/KZT");

        // Stub the method call with appropriate argument matchers
        when(currencyProxy.getExchangeRate(anyString(), anyString())).thenReturn(mockCurrencyDTO);

        // Initialize test data
        List<String> parseStrings = Collections.singletonList("KZT");

        // Mock existing currencies
        HashMap<String, Currency> mockCurrenciesWithDate = new HashMap<>();
        Currency existingCurrency = new Currency();
        existingCurrency.setSymbol("KZT");
        mockCurrenciesWithDate.put("KZT", existingCurrency);

        // Stub the behavior of currencyRepository.findAll()
        when(currencyRepository.findAll()).thenReturn(new ArrayList<>(mockCurrenciesWithDate.values()));

        // Execute the method
        currencyService.parseCurrencyApi();

        // Verify that insertCurrency was called for each parseString
        verify(currencyService, times(1)).insertCurrency(any(), anyString(), any());
    }


}


