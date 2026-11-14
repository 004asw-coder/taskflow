# TaskFlow

REST API для управления проектами и задачами. Проект по дисциплине Java Programming.

## Технологии
Java 21, Spring Boot 3, Spring Data JPA (Hibernate), Spring Security + JWT, PostgreSQL, Maven, Lombok.

## Архитектура
Controller → Service → Repository → PostgreSQL. DTO отделяют API от сущностей. Паттерны: Builder, Repository, DTO.

## Сущности
- User (id, username, email, password, role)
- Project (id, name, description, owner)
- Task (id, title, description, status, deadline, project, assignee)

## Запуск
1. Установить JDK 21 и PostgreSQL.
2. Создать базу `taskdb`.
3. В `src/main/resources/application.properties` указать пароль PostgreSQL.
4. Запустить `TaskflowApplication`. Сервер: http://localhost:8080

## API
| Метод | Адрес | Описание | Доступ |
|---|---|---|---|
| POST | /api/auth/register | Регистрация | все |
| POST | /api/auth/login | Вход, возвращает JWT | все |
| GET/POST | /api/projects | Список / создание проекта | USER, ADMIN |
| GET/PUT | /api/projects/{id} | Получение / обновление | USER, ADMIN |
| DELETE | /api/projects/{id} | Удаление | ADMIN |
| GET/POST | /api/tasks | Список / создание задачи | USER, ADMIN |
| GET/PUT | /api/tasks/{id} | Получение / обновление | USER, ADMIN |
| DELETE | /api/tasks/{id} | Удаление | ADMIN |

Токен передаётся в заголовке `Authorization: Bearer <token>`.

## Тестирование
Коллекция Postman лежит в папке `postman/`.