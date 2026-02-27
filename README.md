# CO2 Emissions Service

Сервис для обработки датасета выбросов CO₂.

Проект реализует несколько вариантов хранения данных:
- CSV (в памяти)
- JDBC (SQL через JdbcTemplate)
- JPA (Hibernate / Spring Data JPA)

Также реализована авторизация через Spring Security.

---

## 🚀 Запуск проекта

### CSV профиль
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=csv
```

- Данные загружаются из CSV в память.
- База данных не используется.
- После перезапуска изменения теряются.

---

### JDBC профиль
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=jdbc
```

- Используется H2 in-memory база данных.
- Таблицы создаются через `schema.sql`.
- При старте данные автоматически импортируются из CSV в БД.

H2 Console:
```
http://localhost:8080/h2-console
```

JDBC URL:
```
jdbc:h2:mem:co2_jdbc
```

---

### JPA профиль
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=jpa
```

- Используется Hibernate + Spring Data JPA.
- Таблицы создаются через `schema.sql`.
- При старте данные импортируются из CSV.
- Пользователи хранятся в таблице `persons`.

H2 Console:
```
http://localhost:8080/h2-console
```

JDBC URL:
```
jdbc:h2:mem:co2_jpa
```

---

## 🔐 Авторизация

Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

### Пользователи:

| Логин | Пароль | Роль |
|-------|--------|------|
| user  | user   | USER |
| admin | admin  | ADMIN |

### Права:

USER:
- GET /api/**

ADMIN:
- POST /api/**
- PUT /api/**
- DELETE /api/**

---

## 📦 Основные CRUD ручки

- GET `/api/emissions`
- GET `/api/emissions/{id}`
- POST `/api/emissions`
- PUT `/api/emissions/{id}`
- DELETE `/api/emissions/{id}`

---

## 📊 Кастомные аналитические ручки

### 1. Список стран
GET `/api/analytics/countries`

### 2. Тренд по стране
GET `/api/analytics/country/{country}/trend?fromYear=1990&toYear=2020`

Пример:
```
/api/analytics/country/Germany/trend?fromYear=2000&toYear=2020
```

### 3. Топ стран по выбросам
GET `/api/analytics/top?year=2019&metric=kilotons&limit=10`

Параметры:
- year — обязательный
- metric — kilotons или perCapita
- limit — количество записей

### 4. Сводка по регионам
GET `/api/analytics/regions?year=2019`

---

## 🧪 Проверка Л7 (пользователи в БД)

В JPA профиле:

1. Открыть H2 Console
2. Выполнить:
```sql
SELECT * FROM persons;
```

3. Отключить пользователя:
```sql
UPDATE persons SET enabled=false WHERE email='user';
```

После этого вход под user станет невозможен.

---

## 🛠 Технологии

- Java 21+
- Spring Boot 3
- Spring Security
- Spring Data JPA
- JdbcTemplate
- H2 Database
- OpenCSV
- Swagger (SpringDoc)

---
