package ru.practicum.order.application.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.order.application.model.OrderCreateOutbox;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OutboxRepositoryTest {

    private static final Long ORDER_ID = 1L;

    @Autowired
    private OutboxRepository repository;

    @Test
    @DisplayName("save() — should save outbox entry")
    void shouldSaveOutboxEntry() {
        OrderCreateOutbox entry = newOutboxEntry();
        OrderCreateOutbox saved = repository.save(entry);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderId()).isEqualTo(ORDER_ID);
    }

    @Test
    @DisplayName("findById() — should return outbox entry if found")
    void shouldFindOutboxEntryById() {
        OrderCreateOutbox saved = repository.save(newOutboxEntry());

        Optional<OrderCreateOutbox> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo(ORDER_ID);
    }

    @Test
    @DisplayName("findById() — should return empty if outbox entry not found")
    void shouldReturnEmptyIfOutboxEntryNotFound() {
        Optional<OrderCreateOutbox> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    private OrderCreateOutbox newOutboxEntry() {
        return OrderCreateOutbox.builder()
                .orderId(ORDER_ID)
                .build();
    }
}