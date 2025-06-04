package ru.practicum.order.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.order.application.client.UserClient;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.mapper.OrderMapper;
import ru.practicum.order.application.model.Order;
import ru.practicum.order.application.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final UserClient userClient;
    private final OrderMapper mapper;

    public OrderResponseDto createOrder(OrderRequestDto dto) {
        log.info("Creating order for user {} with product {}", dto.getUserId(), dto.getProduct());

        if (!userClient.userExists(dto.getUserId())) {
            log.warn("User {} does not exist", dto.getUserId());
            throw new IllegalArgumentException("User does not exist");
        }

        Order order = mapper.toEntity(dto);
        Order saved = repository.save(order);

        return mapper.toDto(saved);
    }
}
