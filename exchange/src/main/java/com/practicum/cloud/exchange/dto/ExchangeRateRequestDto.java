package com.practicum.cloud.exchange.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {

    @NotBlank(message = "Currency from is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyFrom;

    @NotBlank(message = "Currency to is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyTo;

    @NotNull(message = "Buy rate is required")
    @DecimalMin(value = "0.000001", message = "Buy rate must be positive")
    private BigDecimal buyRate;

    @NotNull(message = "Sell rate is required")
    @DecimalMin(value = "0.000001", message = "Sell rate must be positive")
    private BigDecimal sellRate;
}