# Exchange

Микросервис для управления курсами валют в рамках банковского приложения. Предоставляет REST API для создания, получения, обновления и удаления курсов валют.

---

## 🔧 Структура пропкта
  ```
  exchange/
  ├── src/
  │   ├── main/
  │   │   ├── com/practicum/cloud/exchange/
  │   │   │   ├── controller/
  │   │   │   ├── dto/
  │   │   │   ├── mapper/
  │   │   │   ├── model/
  │   │   │   ├── repository/
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
export EXCHANGE_DB_HOST=exchange-db
export EXCHANGE_DB_NAME=exchanges
export EXCHANGE_USERNAME=exchange
export EXCHANGE_PASSWORD=password
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

## endpoints:

- GET /api/rates - получение всех курсов валют для Front UI
- PUT /api/rates/update - обновление курсов от Exchange Generator