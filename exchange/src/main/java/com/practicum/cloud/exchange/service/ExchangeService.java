package com.practicum.cloud.exchange.service;

import com.practicum.cloud.exchange.dto.ExchangeRateRequestDto;
import com.practicum.cloud.exchange.dto.ExchangeRateResponseDto;
import com.practicum.cloud.exchange.mapper.ExchangeRateMapper;
import com.practicum.cloud.exchange.model.ExchangeRate;
import com.practicum.cloud.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    /**
     * Получить все курсы валют
     */
    public List<ExchangeRateResponseDto> getAllRates() {
        log.debug("Getting all exchange rates");
        List<ExchangeRate> rates = exchangeRepository.findAll();
        return exchangeRateMapper.toDtoList(rates);
    }

    /**
     * Получить курс валюты по ID
     */
    public Optional<ExchangeRateResponseDto> getRateById(Long id) {
        log.debug("Getting exchange rate by id: {}", id);
        return exchangeRepository.findById(id)
                .map(exchangeRateMapper::toDto);
    }

    /**
     * Создать новый курс валюты
     */
    @Transactional
    public ExchangeRateResponseDto createRate(ExchangeRateRequestDto requestDto) {
        log.info("Creating new exchange rate from {} to {}",
                requestDto.getCurrencyFrom(), requestDto.getCurrencyTo());

        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(requestDto);
        ExchangeRate savedRate = exchangeRepository.save(exchangeRate);

        log.info("Exchange rate created with id: {}", savedRate.getId());
        return exchangeRateMapper.toDto(savedRate);
    }

    /**
     * Обновить курс валюты
     */
    @Transactional
    public Optional<ExchangeRateResponseDto> updateRate(Long id, ExchangeRateRequestDto requestDto) {
        log.info("Updating exchange rate with id: {}", id);

        return exchangeRepository.findById(id)
                .map(existingRate -> {
                    existingRate.setCurrencyFrom(requestDto.getCurrencyFrom());
                    existingRate.setCurrencyTo(requestDto.getCurrencyTo());
                    existingRate.setBuyRate(requestDto.getBuyRate());
                    existingRate.setSellRate(requestDto.getSellRate());

                    ExchangeRate updatedRate = exchangeRepository.save(existingRate);
                    log.info("Exchange rate updated with id: {}", updatedRate.getId());
                    return exchangeRateMapper.toDto(updatedRate);
                });
    }

    /**
     * Удалить курс валюты
     */
    @Transactional
    public boolean deleteRate(Long id) {
        log.info("Deleting exchange rate with id: {}", id);

        if (exchangeRepository.existsById(id)) {
            exchangeRepository.deleteById(id);
            log.info("Exchange rate deleted with id: {}", id);
            return true;
        }

        log.warn("Exchange rate not found with id: {}", id);
        return false;
    }
}