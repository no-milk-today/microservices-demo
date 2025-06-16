# Bank Microservices Application

Микросервисное приложение "Банк" на базе Spring Boot и Spring Cloud для демонстрации современных архитектурных паттернов в рамках Sprint 9.

## 🎯 Цель проекта

Pet-проект для изучения и демонстрации возможностей Spring Cloud экосистемы:
- Микросервисная архитектура  
- Service Discovery и API Gateway
- Распределенная конфигурация
- OAuth2 аутентификация и авторизация
- Контрактное тестирование
- Отказоустойчивость (Circuit Breaker, Retry, Fallback)

## 🏗️ Архитектура

### Инфраструктурные компоненты
- **Config Server** - Централизованное управление конфигурациями
- **Eureka Server** - Service Discovery для регистрации микросервисов  
- **API Gateway** - Единая точка входа с OAuth2 Client
- **Keycloak** - Сервер авторизации OAuth2/OIDC
- **PostgreSQL** - База данных (Database per Service pattern)
- **Redis** - Кеширование

### Микросервисы
- **Front UI** - Веб-интерфейс (Thymeleaf)
- **Accounts** - Управление пользователями и счетами
- **Cash** - Операции внесения/снятия средств
- **Transfer** - Переводы между счетами
- **Exchange** - Конвертация валют
- **Exchange Generator** - Генерация курсов валют
- **Blocker** - Блокировка подозрительных операций  
- **Notifications** - Система уведомлений

## 🛠️ Технологический стек

- **Java 21**
- **Spring Boot 3.4.4**
- **Gradle** - Система сборки
- **Spring Cloud 2023.0.x** 
  - Spring Cloud Gateway
  - Spring Cloud Config
  - Netflix Eureka
  - Spring Cloud Contract
- **Spring Security** + **OAuth2**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Redis**
- **Keycloak**
- **Resilience4j** - Отказоустойчивость
- **JUnit 5** + **TestContainers** - Тестирование
- **Docker Compose** - Развертывание

## 📁 Структура проекта

```
bank-microservices/
├── config-server/           # Spring Cloud Config Server
├── eureka-server/          # Service Discovery
├── api-gateway/            # Spring Cloud Gateway + OAuth2 Client  
├── exchange-generator/     # Сервис генерации курсов валют
├── exchange/              # Сервис конвертации валют
├── accounts/              # Сервис управления аккаунтами
├── cash/                  # Сервис кеш-операций
├── transfer/              # Сервис переводов
├── blocker/               # Сервис блокировки операций
├── notifications/         # Сервис уведомлений
├── front-ui/              # Веб-интерфейс
├── docker/                # Docker конфигурации
│   ├── docker-compose.yml
│   └── keycloak/
├── contracts/             # Spring Cloud Contract specifications
└── README.md
```

### Запуск инфраструктуры
```bash
# Запуск PostgreSQL, Keycloak, Redis
docker-compose up -d

# Ожидание готовности сервисов
./scripts/wait-for-services.sh
```

### Запуск микросервисов
```bash
# 1. Config Server
cd config-server && ./gradlew bootRun

# 2. Eureka Server  
cd eureka-server && ./gradlew bootRun

# 3. API Gateway
cd api-gateway && ./gradlew bootRun

# 4. Микросервисы (в любом порядке)
cd exchange && ./gradlew bootRun
cd exchange-generator && ./gradlew bootRun
```

## 🔧 Конфигурация портов

| Сервис | Порт | Описание |
|--------|------|----------|
| Config Server | 8888 | Централизованная конфигурация |
| Eureka Server | 8761 | Service Registry |
| API Gateway | 8080 | Единая точка входа |
| Exchange Generator | 8081 | Генератор курсов валют |
| Exchange | 8082 | Сервис конвертации |
| Accounts | 8083 | Управление аккаунтами |
| Cash | 8084 | Кеш-операции |
| Transfer | 8085 | Переводы |
| Blocker | 8086 | Блокировка операций |
| Notifications | 8087 | Уведомления |
| Front UI | 8090 | Веб-интерфейс |
| Keycloak | 9090 | OAuth2 сервер |
| PostgreSQL | 5432 | База данных |
| Redis | 6379 | Кеш |

## 🔐 OAuth2 конфигурация

### Keycloak клиенты
- **gateway-client** - Client Credentials для Gateway
- **exchange-client** - Client Credentials для Exchange Generator

### Scopes  
- `exchange.read` - Чтение курсов валют
- `exchange.write` - Запись курсов валют
- `account.read` - Чтение данных аккаунтов
- `account.write` - Изменение данных аккаунтов

## 📊 База данных

Каждый микросервис имеет отдельную схему в PostgreSQL:
- `accounts_db` - Пользователи и счета
- `cash_db` - Операции с наличными
- `transfer_db` - История переводов  
- `exchange_db` - Курсы валют
- `rates_db` - Сгенерированные курсы
- `blocker_db` - Заблокированные операции
- `notifications_db` - Уведомления

## 🧪 Тестирование

### Запуск тестов
```bash
# Все тесты
./gradlew test

# Контрактные тесты
./gradlew contractTest

# Интеграционные тесты  
./gradlew integrationTest
```

### Spring Cloud Contract
Контракты описаны в `contracts/` директории для каждого провайдера:
- Exchange API контракты
- Accounts API контракты  
- Transfer API контракты

## 🔄 Текущий статус разработки

### ✅ Реализовано
- [x] Архитектура проекта
- [x] Структура репозитория
- [x] Docker Compose конфигурация
- [x] README документация

### 🚧 В разработке  
- [ ] Config Server
- [ ] Eureka Server
- [ ] API Gateway с OAuth2
- [ ] Exchange Generator (Consumer)
- [ ] Exchange (Provider) 
- [ ] Spring Cloud Contract

### 📋 Планируется
- [ ] Остальные микросервисы (Accounts, Cash, Transfer, etc.)
- [ ] Front UI (Thymeleaf)
- [ ] Resilience4j интеграция
- [ ] Мониторинг и логирование
- [ ] Дополнительные тесты

## 🎯 Демонстрируемые паттерны

1. **Микросервисная архитектура** - Независимо развертываемые сервисы
2. **Service Discovery** - Автоматическое обнаружение сервисов через Eureka
3. **API Gateway Pattern** - Единая точка входа с маршрутизацией
4. **Database per Service** - Изолированные схемы данных
5. **OAuth2 Client Credentials** - Межсервисная аутентификация
6. **Contract Testing** - Проверка совместимости API между сервисами
7. **Circuit Breaker Pattern** - Отказоустойчивость при недоступности сервисов
8. **Configuration Externalization** - Централизованное управление настройками