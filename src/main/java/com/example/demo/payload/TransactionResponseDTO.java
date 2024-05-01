package com.example.demo.payload;

import com.example.demo.model.LimitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Data
public class TransactionResponseDTO {

    @JsonProperty("account_from")
    private Long accountFrom;
    @JsonProperty("account_to")
    private Long accountTo;
    @JsonProperty("currency_shortname")
    private String currencyShortname;
    private BigDecimal sum;
    @JsonProperty("expense_category")
    private LimitType expenseCategory;
    @JsonProperty("datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime datetime;

    @JsonProperty("limit_sum")
    private BigDecimal limitSum;
    @JsonProperty("limit_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime limitDatetime;
    @JsonProperty("limit_currency_shortname")
    private String limitCurrencyShortname;

}
