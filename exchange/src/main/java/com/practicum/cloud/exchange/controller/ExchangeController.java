package com.practicum.cloud.exchange.controller;

import com.practicum.cloud.exchange.dto.ExchangeRateRequestDto;
import com.practicum.cloud.exchange.dto.ExchangeRateResponseDto;
import com.practicum.cloud.exchange.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    /**
     * Получить все курсы валют
     * GET /api/rates
     */
    @GetMapping
    public ResponseEntity<List<ExchangeRateResponseDto>> getAllRates() {
        log.info("REST request to get all exchange rates");
        List<ExchangeRateResponseDto> rates = exchangeService.getAllRates();
        return ResponseEntity.ok(rates);
    }

    /**
     * Получить курс валюты по ID
     * GET /api/rates/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeRateResponseDto> getRateById(@PathVariable Long id) {
        log.info("REST request to get exchange rate by id: {}", id);
        return exchangeService.getRateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Создать новый курс валюты
     * POST /api/rates
     */
    @PostMapping
    public ResponseEntity<ExchangeRateResponseDto> createRate(@Valid @RequestBody ExchangeRateRequestDto requestDto) {
        log.info("REST request to create exchange rate from {} to {}",
                requestDto.getCurrencyFrom(), requestDto.getCurrencyTo());

        ExchangeRateResponseDto createdRate = exchangeService.createRate(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRate);
    }

    /**
     * Обновить курс валюты
     * PUT /api/rates/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExchangeRateResponseDto> updateRate(
            @PathVariable Long id,
            @Valid @RequestBody ExchangeRateRequestDto requestDto) {

        log.info("REST request to update exchange rate with id: {}", id);
        return exchangeService.updateRate(id, requestDto)
                .map(updatedRate -> ResponseEntity.ok(updatedRate))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Удалить курс валюты
     * DELETE /api/rates/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        log.info("REST request to delete exchange rate with id: {}", id);

        if (exchangeService.deleteRate(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

