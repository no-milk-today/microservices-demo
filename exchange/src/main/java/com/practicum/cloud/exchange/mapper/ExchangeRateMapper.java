package com.practicum.cloud.exchange.mapper;

import com.practicum.cloud.exchange.dto.ExchangeRateRequestDto;
import com.practicum.cloud.exchange.dto.ExchangeRateResponseDto;
import com.practicum.cloud.exchange.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ExchangeRate toEntity(ExchangeRateRequestDto dto);

    ExchangeRateResponseDto toDto(ExchangeRate exchangeRate);

    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> exchangeRates);
}
