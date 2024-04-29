package com.example.demo.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;

    @Column(precision = 19, scale = 8)
    private BigDecimal closeExchange;

    @Column(precision = 19, scale = 8)
    private BigDecimal previousCloseExchange;

    private Date exchangeDate;

}
