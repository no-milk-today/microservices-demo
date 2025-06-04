# Template Application

`template-application` — это шаблонный микросервис, который реализует паттерн **Microservice Chassis** и служит основой для создания всех остальных сервисов в проекте.

Он содержит базовую структуру, конфигурации, зависимости и инфраструктурные файлы, которые используются во всех микросервисах.

---

## 🔧 Что входит в шаблон

- Базовая структура каталогов:
  ```
  template-application/
  ├── src/
  │   ├── main/
  │   │   ├── java/com/example/template/
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

---

## 🚀 Как использовать

1. Скопируйте директорию `template-application` в новый сервис:
   ```bash
   cp -r template-application my-new-service
   ```

2. Замените имя пакета `ru.practicum.template` на новое имя пакета.
3. Переименуйте проект в `settings.gradle.kts` и `application.yml`.
4. Реализуйте бизнес-логику: контроллеры, модели, сервисы и мапперы.
5. Добавьте сервис в `docker-compose.yml` и `settings.gradle.kts` в корне проекта.

---

## 📦 Быстрый старт

```bash
# Собрать
./gradlew build

# Запустить
docker compose up --build
```

---

Этот шаблон позволяет быстро создавать новые микросервисы в едином стиле, соблюдая архитектурные принципы проекта.
