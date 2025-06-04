# Microservices Demo

Этот проект демонстрирует **микросервисную архитектуру** на базе Spring Boot, PostgreSQL и Docker Compose. Он включает два независимых сервиса:

- **`user-application`** — сервис управления пользователями
- **`order-application`** — сервис управления заказами

## Архитектурные принципы

Проект реализует паттерн **Microservice Chassis** — единый каркас (chassis), в рамках которого микросервисы:

- собраны в одном репозитории;
- используют общие соглашения по сборке, запуску и тестированию;
- имеют единый `docker-compose.yml` и `.env`;
- изолированы друг от друга по базам данных и портам;
- используют единый стек технологий (Spring Boot + PostgreSQL).

Дополнительно применяются следующие архитектурные паттерны:

- **Decompose by Subdomain** — `user-application` и `order-application` реализуют независимые бизнес-области;
- **Database per Service** — у каждого сервиса своя база данных;
- **Single Service per Container** — каждый сервис и каждая БД запускаются в отдельных контейнерах Docker.

## Схема проекта

```
            +------------------+           +-------------------+
            | user-application |           | order-application |
            |  (порт 8081)     | <-------- |  (порт 8082)      |
            +--------+---------+           +--------+----------+
                     |                              |
         +-----------+-----------+     +------------+----------+
         | user-db (PostgreSQL)  |     | order-db (PostgreSQL) |
         |      порт 5433        |     |       порт 5434       |
         +-----------------------+     +-----------------------+
```

## Как начать

1. Убедитесь, что у вас установлен Docker и Docker Compose.
2. Сборка сервисов:
   ```bash
   (cd user-application && ./gradlew clean build)
   (cd order-application && ./gradlew clean build)
   ```

3. Запуск всех сервисов:
   ```bash
   docker-compose up --build
   ```

## Примеры запросов

Создание пользователя:
```bash
curl -X POST http://localhost:8081/users   -H "Content-Type: application/json"   -d '{"name": "Alice", "email": "alice@yandex.ru"}'
```

Получение пользователя:
```bash
curl http://localhost:8081/users/1
```

Создание заказа:
```bash
curl -X POST http://localhost:8082/orders   -H "Content-Type: application/json"   -d '{"userId": 1, "product": "Book"}'
```

## Метрики и мониторинг

Оба сервиса поддерживают Actuator-эндпоинты:
- `GET /actuator/health`
- `GET /actuator/info`

## Локальные переменные окружения

Все переменные хранятся в `.env`. Порты, логины и пароли можно поменять централизованно.

---

> **Примечание:** Внутри каждого микросервиса также есть свой `docker-compose.yml` для изолированного запуска, но рекомендуется использовать общий файл из корневой папки для комплексного развертывания.
