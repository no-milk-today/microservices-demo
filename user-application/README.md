# User Application

Этот микросервис отвечает за управление пользователями. Он предоставляет REST API для создания и получения пользователей по ID. Используется в других микросервисах (например, `order-application`) для проверки существования пользователя.

Архитектурные паттерны:

- **Decompose by Subdomain**
- **Database per Service**
- **Single Service per Container**

---
## Структура проекта:
```
user-application/
├── src/
│   ├── main/
│   │   ├── java/ru/practicum/user/application/
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
export USER_DB_HOST=user-db
export USER_DB_NAME=users
export USER_DB_USERNAME=user
export USER_DB_PASSWORD=password

export SPRING_DATASOURCE_URL=jdbc:postgresql://user-db:5432/users
export SPRING_DATASOURCE_USERNAME=user
export SPRING_DATASOURCE_PASSWORD=password

export SERVER_PORT=8081
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

### Создать пользователя

```bash
curl -X POST http://localhost:8081/users \
     -H "Content-Type: application/json" \
     -d '{"name": "Alice", "email": "alice@yandex.ru"}'
```

### Получить пользователя по ID

```bash
curl http://localhost:8081/users/1
```

---

## Проверка состояния (Actuator)

```bash
curl http://localhost:8081/actuator/health
```
