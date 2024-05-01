package com.example.demo.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppConst {
    public static final String BASE_CURRENCY = "USD";
    public static final Set<String> OTHER_CURRENCY = new HashSet<>(Arrays.asList("KZT", "RUB", "USD"));
    public static final BigDecimal LIMIT_VALUE = BigDecimal.valueOf(1000);
}
