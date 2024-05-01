package com.example.demo.controller;

import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LimitSetRequestDTO;
import com.example.demo.service.LimitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;

    @Operation(summary = "Set new Limit for transactions in month",
            description = "Require type and limit value, type is enum ['PRODUCT','SERVICE']"
    )
    @PostMapping("/limit")
    public ResponseEntity<ApiResponse> setLimit(
            @RequestBody LimitSetRequestDTO request
    ) {
        limitService.addLimit(request);
        return ResponseEntity.ok()
                .body(new ApiResponse("Limit has been successfully updated", true));
    }
}
