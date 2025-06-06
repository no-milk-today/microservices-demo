# Notification Application

Этот микросервис отвечает за нотификации.
---
## Структура проекта:
  ```
  notification-service/
  ├── src/
  │   ├── main/
  │   │   ├── java/ru/practicum/notification/application/
  │   │   │   └── service/
  │   │   └── resources/
  │   │       └── application.yml
  ├── Dockerfile
  ├── docker-compose.yml
  ├── build.gradle.kts
  └── settings.gradle.kts
  ```

## Запуск

Микросервис запускается как отдельный Spring Boot + PostgreSQL сервис с помощью Docker Compose.

### Шаг 1: Установить переменные окружения

Создайте `.env` файл или выполните команды ниже в терминале:

```bash
export ORDER_DB_HOST=order-db
export ORDER_DB_NAME=orders
export ORDER_DB_USERNAME=order
export ORDER_DB_PASSWORD=password

export ORDER_SERVICE_PORT=8082
```

### Шаг 2: Собрать JAR-файл

```bash
./gradlew clean build
```

### Шаг 3: Запустить сервис

```bash
docker-compose up --build
```

---

## Примеры запросов

### Создать заказ (предполагается, что пользователь с ID = 1 существует)

```bash
curl -X POST http://localhost:8082/orders \
     -H "Content-Type: application/json" \
     -d '{"userId": 1, "product": "Test Product"}'
```

---

## Проверка состояния (Actuator)

```bash
curl http://localhost:8083/actuator/health
```
