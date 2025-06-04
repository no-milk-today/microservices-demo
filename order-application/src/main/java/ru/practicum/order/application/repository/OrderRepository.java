package ru.practicum.order.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.order.application.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
