package com.example.demo.proxy;

import com.example.demo.payload.CurrencyConversionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currencies", url = "${app.service.url}")
public interface CurrencyProxy {

    @GetMapping("/exchange_rate")
    CurrencyConversionDTO getExchangeRate(
            @RequestParam("apikey") String apiKey,
            @RequestParam("symbol") String symbol);
}
