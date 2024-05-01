package com.example.demo.controller;


import com.example.demo.model.Currency;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.service.CurrencyService;
import com.example.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final CurrencyService currencyService;

    @Operation(summary = "Add new transaction",
            description = "Save transaction into database. Don't need post by datetime sorted"
    )
    @PostMapping("/transaction")
    public ResponseEntity<ApiResponse> createTransaction(
            @RequestBody TransactionRequestDTO requestDTO) {

        Currency currency = currencyService.getCurrencyBySymbol(requestDTO.getCurrencyShortname());
        transactionService.addTransaction(requestDTO, currency);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse("Transaction successfully has been created", true));
    }

    @Operation(summary = "Get All Limit Exceeded Transactions")
    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions() {
        return ResponseEntity.ok(transactionService.getExceededTransactions());
    }
}
