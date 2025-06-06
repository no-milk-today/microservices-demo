package ru.practicum.notification.service;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderCreateReceiver {

    private static final Logger log = LoggerFactory.getLogger(OrderCreateReceiver.class);

    private final Connection connection;

    public OrderCreateReceiver(@Value("${application.nats.url}") String natsConnectionUrl,
                               @Value("${application.notifications.order.topic-name}") String orderTopicName) throws IOException, InterruptedException {
        this.connection = Nats.connect(natsConnectionUrl);
        // Создаём Dispatcher, который будет обрабатывать новые сообщения
        connection.createDispatcher(this::handleMessage).subscribe(orderTopicName);
    }

    private void handleMessage(Message message) {
        // Обработка новых сообщений
        String messageData = new String(message.getData());
        log.info("Создан новый заказ: {}", messageData);
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        // Закрываем соединение при удалении бина из контекста
        connection.close();
    }
}
