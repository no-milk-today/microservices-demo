package com.practicum.cloud.exchange.dto;

import com.practicum.cloud.exchange.model.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private CurrencyCode currency;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
}

