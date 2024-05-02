package com.example.demo.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


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
    private OffsetDateTime datetime;


    @ManyToOne
    @JoinColumn(name = "limit_id", nullable = false)
    private Limit limit;

}
