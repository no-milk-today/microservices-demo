package com.practicum.cloud.exchange.repository;

import com.practicum.cloud.exchange.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<ExchangeRate, Long> {
}
