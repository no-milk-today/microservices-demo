package com.practicum.cloud.exchange.controller;

import com.practicum.cloud.exchange.dto.ExchangeRateDto;
import com.practicum.cloud.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping
    public List<ExchangeRateDto> getAllRates() {
        return exchangeService.getAllRates();
    }

    @PutMapping("/update")
    public void updateRates(@RequestBody List<ExchangeRateDto> rates) {
        exchangeService.updateRates(rates);
    }
}

