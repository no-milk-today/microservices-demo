package com.practicum.cloud.exchange.service;

import com.practicum.cloud.exchange.dto.ExchangeRateDto;
import com.practicum.cloud.exchange.mapper.ExchangeRateMapper;
import com.practicum.cloud.exchange.model.CurrencyCode;
import com.practicum.cloud.exchange.model.ExchangeRate;
import com.practicum.cloud.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final ExchangeRateMapper exchangeMapper;

    public List<ExchangeRateDto> getAllRates() {
        List<ExchangeRate> rates = exchangeRepository.findAll();
        // Добавляем RUB как базовую валюту с курсом 1.0
        List<ExchangeRateDto> result = new ArrayList<>();
        result.add(new ExchangeRateDto(CurrencyCode.RUB, BigDecimal.ONE, BigDecimal.ONE));
        result.addAll(exchangeMapper.toDtoList(rates));
        log.debug("Возвращено {} курс(ов) обмена, включая базовую валюту", result.size());
        return result;
    }

    public void updateRates(List<ExchangeRateDto> rates) {
        log.info("Удаление всех старых записей валют");
        exchangeRepository.deleteAll(); // Простое обновление - удаляем все старые
        List<ExchangeRate> entities = exchangeMapper.toEntityList(
                rates.stream()
                     .filter(rate -> !CurrencyCode.RUB.equals(rate.getCurrency()))
                     .collect(Collectors.toList())
        );
        exchangeRepository.saveAll(entities);
        log.info("Обновление курсов завершено. Сохранено {} новых записей", entities.size());
    }

}