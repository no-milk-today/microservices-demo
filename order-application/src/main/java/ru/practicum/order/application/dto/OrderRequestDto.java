package ru.practicum.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Long userId;
    private String product;
}
