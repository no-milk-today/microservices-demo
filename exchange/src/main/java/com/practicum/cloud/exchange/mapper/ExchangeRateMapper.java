package com.practicum.cloud.exchange.mapper;

import com.practicum.cloud.exchange.dto.ExchangeRateDto;
import com.practicum.cloud.exchange.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ExchangeRate toEntity(ExchangeRateDto dto);

    ExchangeRateDto toDto(ExchangeRate entity);

    List<ExchangeRateDto> toDtoList(List<ExchangeRate> entities);

    List<ExchangeRate> toEntityList(List<ExchangeRateDto> dtos);
}

