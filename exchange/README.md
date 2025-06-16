# Exchange

Сервис конвертации валют, хранит информацию о конвертации валюты при её покупке/продаже (базовой валютой считается RUB, её конвертация при продаже/покупке равна 1).
Фронт выполняет REST-запросы (в формате JSON) из блока курсов валют в сервис Exchange для получения информации о курсах валют.

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

- Стандартные зависимости:
    - Spring Boot Web
    - Spring Data JPA
    - PostgreSQL Driver
    - Lombok
    - MapStruct

- Конфигурация подключения к базе данных через переменные окружения
- Dockerfile для сборки
- docker-compose.yml для локального запуска с PostgreSQL


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

- GET /api/rates - получение всех курсов валют
- GET /api/rates/{id} - получение курса по ID
- POST /api/rates - создание нового курса
- PUT /api/rates/{id} - обновление существующего курса
- DELETE /api/rates/{id} - удаление курса