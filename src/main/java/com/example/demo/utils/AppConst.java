package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AppConst {
    public static final String BASE_CURRENCY = "USD";
    public static final List<String> OTHER_CURRENCY = Arrays.asList("KZT", "RUB","USD");
    public static final BigDecimal LIMIT_VALUE = BigDecimal.valueOf(1000);
}
