package com.example.demo.model;


import com.example.demo.utils.AppConst;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "limits")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal limitValue = AppConst.LIMIT_VALUE;
    private BigDecimal remainValue;


    @OneToMany(mappedBy="limit")
    private Set<Transaction> transactions;


    @Enumerated(EnumType.STRING)
    private LimitType type;

    @CreationTimestamp
    private OffsetDateTime createDate;
    @UpdateTimestamp
    private OffsetDateTime updatedDate;
}
