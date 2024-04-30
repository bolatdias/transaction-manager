package com.example.demo.controller;

import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LimitSetRequest;
import com.example.demo.service.LimitService;
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

    @PostMapping("/limit")
    public ResponseEntity<ApiResponse> setLimit(
            @RequestBody LimitSetRequest request
    ) {
        limitService.addLimit(request);
        return ResponseEntity.ok()
                .body(new ApiResponse("Limit has been successfully updated", true));
    }
}
