package com.example.demo.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;


@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    private Long accountFrom;



    private Long accountTo;
    private BigDecimal sum;
    @Enumerated(EnumType.STRING)
    private LimitType expenseCategory;
    private OffsetDateTime datetime;


    @ManyToOne
    @JoinColumn(name = "limit_id", nullable = false)
    private Limit limit;

}
