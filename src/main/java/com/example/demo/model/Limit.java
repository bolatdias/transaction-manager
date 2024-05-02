package com.example.demo.model;


import com.example.demo.utils.AppConst;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "limits")
@Data
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal limitValue = AppConst.LIMIT_VALUE;


    @OneToMany(mappedBy = "limit")
    private Set<Transaction> transactions;


    @Enumerated(EnumType.STRING)
    private LimitType type;

    @CreationTimestamp
    private OffsetDateTime createdDate;

    @Transient
    private OffsetDateTime lastUpdateDate;


}
