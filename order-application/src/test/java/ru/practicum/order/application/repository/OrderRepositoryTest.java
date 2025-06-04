package ru.practicum.order.application.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.order.application.model.Order;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final String PRODUCT = "Laptop";

    @Autowired
    private OrderRepository repository;

    @Test
    @DisplayName("save() — должен сохранить заказ")
    void shouldSaveOrder() {
        Order order = newOrder();
        Order saved = repository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(USER_ID);
        assertThat(saved.getProduct()).isEqualTo(PRODUCT);
    }

    @Test
    @DisplayName("findById() — должен вернуть заказ, если найден")
    void shouldFindOrderById() {
        Order saved = repository.save(newOrder());

        Optional<Order> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(USER_ID);
        assertThat(result.get().getProduct()).isEqualTo(PRODUCT);
    }

    @Test
    @DisplayName("findById() — должен вернуть пусто, если заказ не найден")
    void shouldReturnEmptyIfOrderNotFound() {
        Optional<Order> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    private Order newOrder() {
        return Order.builder()
                .userId(OrderRepositoryTest.USER_ID)
                .product(OrderRepositoryTest.PRODUCT)
                .build();
    }
}
