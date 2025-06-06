package ru.practicum.order.application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.order.application.model.OrderCreateOutbox;

public interface OutboxRepository extends JpaRepository<OrderCreateOutbox, Long> {
}
