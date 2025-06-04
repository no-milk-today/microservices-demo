package ru.practicum.order.application.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.model.Order;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private static final Long ORDER_ID = 1L;
    private static final Long USER_ID = 42L;
    private static final String PRODUCT = "Book";

    private final OrderMapper mapper = new OrderMapperImpl();

    @Test
    @DisplayName("toEntity() — должен маппить OrderRequestDto в Order без id")
    void shouldMapToEntity() {
        OrderRequestDto dto = OrderRequestDto.builder()
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        Order order = mapper.toEntity(dto);

        assertThat(order.getId()).isNull();
        assertThat(order.getUserId()).isEqualTo(USER_ID);
        assertThat(order.getProduct()).isEqualTo(PRODUCT);
    }

    @Test
    @DisplayName("toDto() — должен маппить Order в OrderResponseDto")
    void shouldMapToDto() {
        Order order = Order.builder()
                .id(ORDER_ID)
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        OrderResponseDto dto = mapper.toDto(order);

        assertThat(dto.getId()).isEqualTo(ORDER_ID);
        assertThat(dto.getUserId()).isEqualTo(USER_ID);
        assertThat(dto.getProduct()).isEqualTo(PRODUCT);
    }
}
