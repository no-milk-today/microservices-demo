package com.practicum.cloud.exchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "exchange_rates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String currency; // USD, CNY (RUB не хранится, так как базовая = 1)

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal buyRate;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal sellRate;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
