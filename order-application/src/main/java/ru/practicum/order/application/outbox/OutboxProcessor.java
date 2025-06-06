package ru.practicum.order.application.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Nats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.practicum.order.application.model.Order;
import ru.practicum.order.application.model.OrderCreateOutbox;
import ru.practicum.order.application.repository.OrderRepository;
import ru.practicum.order.application.repository.OutboxRepository;

import java.util.List;

@Slf4j
@Service
public class OutboxProcessor {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final OrderRepository orderRepository;
    private final String natsUrl;
    private final String topicName;
    private final int limit;

    /**
     * Конструктор автоматически инжектирует:
     *  - объект ObjectMapper (для сериализации заказа в JSON)
     *  - репозитории для outbox и order
     *  - параметры из application.yml
     */
    public OutboxProcessor(ObjectMapper objectMapper,
                           OutboxRepository outboxRepository,
                           OrderRepository orderRepository,
                           @Value("${application.nats.url}") String natsUrl,
                           @Value("${application.outbox.order.topic-name}") String topicName,
                           @Value("${application.outbox.order.limit:5}") int limit) {
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
        this.orderRepository = orderRepository;
        this.natsUrl = natsUrl;
        this.topicName = topicName;
        this.limit = limit;
    }

    /**
     * Каждую секунду пытаемся обработать до batchSize записей из outbox.
     */
    @Scheduled(fixedDelayString = "PT1S")
    public void process() {
        Page<OrderCreateOutbox> outboxPage = outboxRepository.findAll(Pageable.ofSize(limit));
        List<OrderCreateOutbox> outboxEntries = outboxPage.getContent();

        if (outboxEntries.isEmpty()) {
            return; // нечего отправлять
        }

        // Собираем список orderId, по которым нужно получить заказы
        List<Long> orderIds = outboxEntries.stream()
                .map(OrderCreateOutbox::getOrderId)
                .toList();

        // Загружаем соответствующие сущности Order
        List<Order> orders = orderRepository.findAllById(orderIds);

        // Подключаемся к NATS (один раз за batch)
        try (Connection nats = Nats.connect(natsUrl)) {
            for (Order order : orders) {
                try {
                    // Сериализуем заказ в JSON
                    byte[] payload = objectMapper.writeValueAsBytes(order);

                    // Публикуем в указанный топик
                    nats.publish(topicName, payload);
                    log.info("Отправили заказ {} в NATS-топик '{}'", order.getId(), topicName);
                } catch (Exception e) {
                    log.error("Ошибка при отправке заказа {} в NATS", order.getId(), e);
                    // Если при сериализации/публикации произошла ошибка, просто пропускаем его сейчас.
                    // Можно дополнительно вести логику retry или менять статус в outbox-таблице.
                }
            }
        } catch (Exception connEx) {
            log.error("Не удалось подключиться к NATS по URL {}: {}", natsUrl, connEx.getMessage());
            // Если не удалось подключиться к брокеру, придётся подождать следующую итерацию Scheduler-а.
            return;
        }

        // Если сюда дошли — предполагаем, что всю пачку мы «отправили» (либо пропустили неудачные).
        // Удаляем обработанные записи из Outbox
        List<Long> toDeleteIds = outboxEntries.stream()
                .map(OrderCreateOutbox::getId)
                .toList();
        outboxRepository.deleteAllByIdInBatch(toDeleteIds);
        log.debug("Удалили {} записей из Outbox", toDeleteIds.size());
    }
}

