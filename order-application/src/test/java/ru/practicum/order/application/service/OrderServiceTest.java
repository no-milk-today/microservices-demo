package ru.practicum.order.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.order.application.client.UserClient;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.mapper.OrderMapper;
import ru.practicum.order.application.model.Order;
import ru.practicum.order.application.model.OrderCreateOutbox;
import ru.practicum.order.application.repository.OrderRepository;
import ru.practicum.order.application.repository.OutboxRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Long USER_ID = 1L;
    private static final String PRODUCT = "Book";
    private static final Long ORDER_ID = 1L;

    @Mock
    private UserClient userClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderService underTest;

    @Test
    @DisplayName("createOrder() — должен создать заказ и сохранить outbox entry, если пользователь существует")
    void shouldCreateOrderWhenUserExists() {
        OrderRequestDto request = OrderRequestDto.builder()
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        Order orderToSave = Order.builder()
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        Order savedOrder = Order.builder()
                .id(ORDER_ID)
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .id(ORDER_ID)
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        when(userClient.userExists(USER_ID)).thenReturn(true);
        when(mapper.toEntity(request)).thenReturn(orderToSave);
        when(orderRepository.save(orderToSave)).thenReturn(savedOrder);
        when(mapper.toDto(savedOrder)).thenReturn(responseDto);

        OrderResponseDto response = underTest.createOrder(request);

        assertThat(response.getId()).isEqualTo(ORDER_ID);
        assertThat(response.getUserId()).isEqualTo(USER_ID);
        assertThat(response.getProduct()).isEqualTo(PRODUCT);

        ArgumentCaptor<OrderCreateOutbox> captor = ArgumentCaptor.forClass(OrderCreateOutbox.class);
        verify(outboxRepository).save(captor.capture());
        OrderCreateOutbox savedOutbox = captor.getValue();
        assertThat(savedOutbox.getOrderId()).isEqualTo(ORDER_ID);
    }

    @Test
    @DisplayName("createOrder() — должен выбросить исключение, если пользователь не найден")
    void shouldThrowWhenUserNotFound() {
        OrderRequestDto request = OrderRequestDto.builder()
                .userId(999L)
                .product("Game")
                .build();

        when(userClient.userExists(999L)).thenReturn(false);

        assertThatThrownBy(() -> underTest.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");

        verifyNoInteractions(mapper, orderRepository, outboxRepository);
    }
}
