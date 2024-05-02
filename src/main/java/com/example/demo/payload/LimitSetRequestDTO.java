package com.example.demo.payload;


import com.example.demo.model.LimitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LimitSetRequestDTO {
    private BigDecimal limit;
    private LimitType type;
}
