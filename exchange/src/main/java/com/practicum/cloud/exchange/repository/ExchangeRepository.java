package com.practicum.cloud.exchange.repository;

import com.practicum.cloud.exchange.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRate, Long> {

    /**
     * Найти курс валют по паре валют
     */
    Optional<ExchangeRate> findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);

    /**
     * Найти все курсы для определенной валюты (от какой валюты)
     */
    List<ExchangeRate> findByCurrencyFrom(String currencyFrom);

    /**
     * Найти все курсы для определенной валюты (к какой валюте)
     */
    List<ExchangeRate> findByCurrencyTo(String currencyTo);

    /**
     * Получить все курсы, отсортированные по дате создания (последние первыми)
     */
    @Query("SELECT er FROM ExchangeRate er ORDER BY er.createdAt DESC")
    List<ExchangeRate> findAllOrderByCreatedAtDesc();

    /**
     * Найти курсы валют по списку валют
     */
    @Query("SELECT er FROM ExchangeRate er WHERE er.currencyFrom IN :currencies OR er.currencyTo IN :currencies")
    List<ExchangeRate> findByCurrenciesIn(@Param("currencies") List<String> currencies);

    /**
     * Проверить существование курса для валютной пары
     */
    boolean existsByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);
}
