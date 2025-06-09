# Order Application

Этот микросервис отвечает за управление заказами. Перед созданием заказа он обращается к `user-application`, чтобы проверить, существует ли пользователь с указанным `userId`.

Архитектурные паттерны:
- **Decompose by Subdomain**
- **Database per Service**
- **Single Service per Container**

---
## Структура проекта:
```
order-application/
├── src/
│   ├── main/
│   │   ├── java/ru/practicum/order/application/
│   │   │   ├── client/               # WebClient для обращения к user-service
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.yml
├── Dockerfile                # Шаблон сборки через Docker
├── docker-compose.yml        # Шаблон конфигурации для локального запуска
└── build.gradle.kts          # Зависимости и сборка
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

export USER_CLIENT_BASE_URL=http://host.docker.internal:8081
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

### Login request (to get JWT token) in `user-application`

```bash
curl -X POST http://localhost:8081/api/login \
     -H "Content-Type: application/json" \
     -d '{"username": "alice@yandex.ru", "password": "strongPass123"}'
```

### Создать заказ (предполагается, что пользователь с ID = 1 существует)
Сюда подставить JWT-токен, полученный на предыдущем шаге, в заголовок `Authorization`

```bash
curl -X POST http://localhost:8082/orders \
     -H "Content-Type: application/json" \
     -d '{"userId": 1, "product": "Test Product"}'
```

---

## Проверка состояния (Actuator)

```bash
curl http://localhost:8082/actuator/health
```
