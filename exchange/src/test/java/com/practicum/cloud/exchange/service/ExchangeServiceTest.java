package com.practicum.cloud.exchange.service;

import com.practicum.cloud.exchange.dto.ExchangeRateDto;
import com.practicum.cloud.exchange.mapper.ExchangeRateMapper;
import com.practicum.cloud.exchange.model.CurrencyCode;
import com.practicum.cloud.exchange.model.ExchangeRate;
import com.practicum.cloud.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;
    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @InjectMocks
    private ExchangeService underTest;

    @Test
    void getAllRates_shouldReturnWithRub() {
        when(exchangeRepository.findAll()).thenReturn(Collections.emptyList());
        when(exchangeRateMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        var rates = underTest.getAllRates();

        assertThat(rates).hasSize(1);
        assertThat(rates.getFirst().getCurrency()).isEqualTo(CurrencyCode.RUB);
        assertThat(rates.getFirst().getBuyRate()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(rates.getFirst().getSellRate()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void updateRates_shouldDeleteOldAndSaveNew() {
        var dto = new ExchangeRateDto(CurrencyCode.USD, new BigDecimal("90.00"), new BigDecimal("91.00"));
        var entity = new ExchangeRate();
        when(exchangeRateMapper.toEntityList(anyList())).thenReturn(Collections.singletonList(entity));

        underTest.updateRates(Collections.singletonList(dto));

        verify(exchangeRepository).deleteAll();
        verify(exchangeRepository).saveAll(anyList());
    }
}
