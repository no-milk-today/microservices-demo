package ru.practicum.order.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toDto(Order entity);
}
