package com.example.demo.payload;


import com.example.demo.model.LimitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;


public class TransactionRequestDTO {
    @JsonProperty("account_from")
    private Long accountFrom;
    @JsonProperty("account_to")
    private Long accountTo;
    @JsonProperty("currency_shortname")
    private String currencyShortname;

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public String getCurrencyShortname() {
        return currencyShortname;
    }

    public void setCurrencyShortname(String currencyShortname) {
        this.currencyShortname = currencyShortname;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public LimitType getType() {
        return type;
    }

    public void setType(LimitType type) {
        this.type = type;
    }

    public OffsetDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(OffsetDateTime datetime) {
        this.datetime = datetime;
    }

    private BigDecimal sum;
    @JsonProperty("expense_category")
    private LimitType type;

    @JsonProperty("datetime")
    private OffsetDateTime datetime;
}
