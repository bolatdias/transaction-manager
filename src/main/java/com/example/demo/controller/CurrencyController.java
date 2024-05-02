package com.example.demo.controller;


import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.CurrencyDTO;
import com.example.demo.payload.PagedResponse;
import com.example.demo.service.CurrencyService;
import com.example.demo.utils.AppConst;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @Operation(summary = "Parse Currency from API",
            description = "Backend automatically parse daly in works days, but you can manually run this"
    )
    @PostMapping("/currency")
    public ResponseEntity<ApiResponse> parseCurrency() {
        currencyService.parseCurrencyApi(AppConst.OTHER_CURRENCY);
        return ResponseEntity.ok()
                .body(new ApiResponse("Currency parsing has successfully", true));
    }
    @Operation(summary = "Get All Currency")
    @GetMapping("/currency")
    public ResponseEntity<PagedResponse<CurrencyDTO>> getCurrency(
            @ParameterObject Pageable pageable
    ) {
        PagedResponse<CurrencyDTO> response = currencyService.getAll(pageable);
        return ResponseEntity.ok()
                .body(response);
    }
}
