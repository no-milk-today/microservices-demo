# Notification Application

Этот микросервис отвечает за нотификации. Подписывается на Topic в NATS и читает из него сообщения.
Когда сообщение попадёт в `NATS`, в `notification-service`, подписанный на топик `order.created`, будет логировать информацию.
Если при отправке NATS упало, запись останется в Outbox и будет повторно прочитана на следующем цикле. 

## Запуск
Микросервис запускается как отдельный Spring Boot сервис с помощью Docker Compose.

### Шаг 1: Прописать env переменную
```bash
export NOTIFICATION_SERVICE_PORT=8082
```

### Шаг 2: Собрать JAR-файл

```bash
./gradlew clean build
```

### Шаг 3: Запустить сервис

```bash
docker-compose up --build
```

## Проверка состояния (Actuator)

```bash
curl http://localhost:8083/actuator/health
```
