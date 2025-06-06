package ru.practicum.order.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.order.application.client.UserClient;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.mapper.OrderMapper;
import ru.practicum.order.application.model.Order;
import ru.practicum.order.application.model.OrderCreateOutbox;
import ru.practicum.order.application.repository.OrderRepository;
import ru.practicum.order.application.repository.OutboxRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final UserClient userClient;
    private final OrderMapper mapper;

    /**
     * Теперь метод помечен как @Transactional: создание заказа и запись в outbox пойдут в одной транзакции.
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        log.info("Creating order for user {} with product {}", dto.getUserId(), dto.getProduct());

        // 1. Проверяем существование пользователя
        if (!userClient.userExists(dto.getUserId())) {
            log.warn("User {} does not exist", dto.getUserId());
            throw new IllegalArgumentException("User does not exist");
        }

        // 2. Сохраняем сам Order
        Order order = mapper.toEntity(dto);
        Order saved = orderRepository.save(order);

        // 3. Создаём Outbox-запись (её тоже сохраняем в той же транзакции)
        OrderCreateOutbox outboxEntry = OrderCreateOutbox.builder()
                .orderId(saved.getId())
                .build();
        outboxRepository.save(outboxEntry);

        // 4. Возвращаем DTO
        return mapper.toDto(saved);
    }
}
