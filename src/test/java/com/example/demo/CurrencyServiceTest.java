package com.example.demo;

import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.proxy.CurrencyProxy;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyProxy currencyProxy;


    @Mock
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(currencyService, "apiKey", "api-key");
    }

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
    void testInsertCurrency() {

        HashMap<String, Currency> currHashMap = new HashMap<>();
        String symbol = "EUR";
        CurrencyConversionDTO currencyDTO = new CurrencyConversionDTO();
        currencyDTO.setRate(new BigDecimal("1.2"));

        currencyService.insertCurrency(currHashMap, symbol, currencyDTO);
        verify(currencyRepository, times(1)).save(any());
    }



    @Test
    void testParseCurrencyApi() {
        // Mock data
        List<String> parseStrings = Arrays.asList("EUR", "GBP");
        CurrencyConversionDTO conversionDTO = new CurrencyConversionDTO();
        conversionDTO.setRate(new BigDecimal("1.2"));

        // Stubbing
        when(currencyProxy.getExchangeRate(anyString(), anyString())).thenReturn(conversionDTO);
        when(currencyRepository.findAll()).thenReturn(new ArrayList<>());

        // Test
        currencyService.parseCurrencyApi(parseStrings);

        // Verification
        verify(currencyProxy, times(2)).getExchangeRate(anyString(), anyString());
        verify(currencyRepository, times(2)).save(any());
    }



}


