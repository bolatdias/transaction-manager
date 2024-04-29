package com.example.demo.controller;


import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.payload.CurrencyConversionInputDTO;
import com.example.demo.service.CurrencyService;
import com.example.demo.utils.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currency")
    public CurrencyConversionDTO getCurrency() {
        CurrencyConversionInputDTO inputDTO = new CurrencyConversionInputDTO("RUB/" + AppConst.BASE_CURRENCY, new BigDecimal("100.00"));

        currencyService.parseCurrency();
        return currencyService.getExchangeRate(inputDTO);
    }
}
