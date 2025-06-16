package com.practicum.cloud.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDto {

    private Long id;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
    private LocalDateTime createdAt;
}
