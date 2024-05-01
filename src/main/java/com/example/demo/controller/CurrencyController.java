package com.example.demo.controller;


import com.example.demo.payload.ApiResponse;
import com.example.demo.service.CurrencyService;
import com.example.demo.utils.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currency")
    public ResponseEntity<ApiResponse> parseCurrency() {
        currencyService.parseCurrencyApi(AppConst.OTHER_CURRENCY);
        return ResponseEntity.ok()
                .body(new ApiResponse("Currency parsing has successfully", true));
    }
}
