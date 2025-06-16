package com.practicum.cloud.exchange.service;

import com.practicum.cloud.exchange.dto.ExchangeRateRequestDto;
import com.practicum.cloud.exchange.dto.ExchangeRateResponseDto;
import com.practicum.cloud.exchange.mapper.ExchangeRateMapper;
import com.practicum.cloud.exchange.model.ExchangeRate;
import com.practicum.cloud.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @InjectMocks
    private ExchangeService underTest;

    private ExchangeRate exchangeRateEntity;
    private ExchangeRateRequestDto requestDto;
    private ExchangeRateResponseDto responseDto;

    @BeforeEach
    void setUp() {
        exchangeRateEntity = ExchangeRate.builder()
                .id(1L)
                .currencyFrom("USD")
                .currencyTo("RUB")
                .buyRate(new BigDecimal("92.50"))
                .sellRate(new BigDecimal("93.50"))
                .createdAt(LocalDateTime.now())
                .build();

        requestDto = ExchangeRateRequestDto.builder()
                .currencyFrom("USD")
                .currencyTo("RUB")
                .buyRate(new BigDecimal("92.50"))
                .sellRate(new BigDecimal("93.50"))
                .build();

        responseDto = ExchangeRateResponseDto.builder()
                .id(1L)
                .currencyFrom("USD")
                .currencyTo("RUB")
                .buyRate(new BigDecimal("92.50"))
                .sellRate(new BigDecimal("93.50"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllRatesTest() {
        // Given
        List<ExchangeRate> rates = Collections.singletonList(exchangeRateEntity);
        List<ExchangeRateResponseDto> expectedDtos = Collections.singletonList(responseDto);

        when(exchangeRepository.findAll()).thenReturn(rates);
        when(exchangeRateMapper.toDtoList(rates)).thenReturn(expectedDtos);

        // When
        List<ExchangeRateResponseDto> result = underTest.getAllRates();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCurrencyFrom()).isEqualTo("USD");
        assertThat(result.getFirst().getCurrencyTo()).isEqualTo("RUB");

        verify(exchangeRepository).findAll();
        verify(exchangeRateMapper).toDtoList(rates);
    }

    @Test
    void getRateById_WhenExists() {
        // Given
        when(exchangeRepository.findById(1L)).thenReturn(Optional.of(exchangeRateEntity));
        when(exchangeRateMapper.toDto(exchangeRateEntity)).thenReturn(responseDto);

        // When
        Optional<ExchangeRateResponseDto> result = underTest.getRateById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(exchangeRepository).findById(1L);
        verify(exchangeRateMapper).toDto(exchangeRateEntity);
    }

    @Test
    void getRateById_WhenNotExists() {
        // Given
        when(exchangeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<ExchangeRateResponseDto> result = underTest.getRateById(1L);

        // Then
        assertThat(result).isEmpty();

        verify(exchangeRepository).findById(1L);
        verify(exchangeRateMapper, never()).toDto(any());
    }

    @Test
    void createRateTest() {
        // Given
        when(exchangeRateMapper.toEntity(requestDto)).thenReturn(exchangeRateEntity);
        when(exchangeRepository.save(exchangeRateEntity)).thenReturn(exchangeRateEntity);
        when(exchangeRateMapper.toDto(exchangeRateEntity)).thenReturn(responseDto);

        // When
        ExchangeRateResponseDto result = underTest.createRate(requestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCurrencyFrom()).isEqualTo("USD");
        assertThat(result.getCurrencyTo()).isEqualTo("RUB");

        verify(exchangeRateMapper).toEntity(requestDto);
        verify(exchangeRepository).save(exchangeRateEntity);
        verify(exchangeRateMapper).toDto(exchangeRateEntity);
    }

    @Test
    void deleteRate_WhenExists() {
        // Given
        when(exchangeRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = underTest.deleteRate(1L);

        // Then
        assertThat(result).isTrue();

        verify(exchangeRepository).existsById(1L);
        verify(exchangeRepository).deleteById(1L);
    }

    @Test
    void deleteRate_WhenNotExists() {
        // Given
        when(exchangeRepository.existsById(1L)).thenReturn(false);

        // When
        boolean result = underTest.deleteRate(1L);

        // Then
        assertThat(result).isFalse();

        verify(exchangeRepository).existsById(1L);
        verify(exchangeRepository, never()).deleteById(any());
    }
}
