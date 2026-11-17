# TaskFlow

REST API для управления проектами и задачами. Финальный проект по курсу Java Programming.

## Технологии
Java 21, Spring Boot 4, Spring Data JPA (Hibernate), Spring Security + JWT, PostgreSQL, Maven, Lombok, springdoc-openapi (Swagger), JUnit 5 + Mockito.

## Архитектура
Controller → Service → Repository → PostgreSQL.
DTO отделяют API от сущностей. Паттерны: Builder (Lombok), Repository (Spring Data), DTO.

## Сущности
- User (id, username, email, password, role)
- Project (id, name, description, owner)
- Task (id, title, description, status, deadline, project, assignee)

## Роли
- USER: создаёт и просматривает проекты и задачи, обновляет их.
- ADMIN: дополнительно удаляет проекты и задачи.

## Запуск
1. Установить JDK 21 и PostgreSQL.
2. Создать базу `taskdb`.
3. В `../../../../resources/application.properties` указать пароль PostgreSQL.
4. Запустить `TaskflowApplication`. Сервер: http://localhost:8080
5. Swagger UI: http://localhost:8080/swagger-ui/index.html

## Как получить права ADMIN
Регистрация создаёт USER. Для ADMIN выполнить в базе:
`UPDATE users SET role = 'ADMIN' WHERE username = 'имя';`
и заново войти.

## API
| Метод | Адрес | Описание | Доступ |
|---|---|---|---|
| POST | /api/auth/register | Регистрация | все |
| POST | /api/auth/login | Вход, возвращает JWT | все |
| GET/POST | /api/projects | Список / создание проекта | USER, ADMIN |
| GET/PUT | /api/projects/{id} | Получение / обновление | USER, ADMIN |
| DELETE | /api/projects/{id} | Удаление | ADMIN |
| GET | /api/tasks | Поиск: title, status, projectId, page, size | USER, ADMIN |
| POST | /api/tasks | Создание задачи | USER, ADMIN |
| GET/PUT | /api/tasks/{id} | Получение / обновление | USER, ADMIN |
| DELETE | /api/tasks/{id} | Удаление | ADMIN |

Токен передаётся в заголовке `Authorization: Bearer <token>`.

## Тесты
Запуск: `TaskServiceTest` (JUnit 5 + Mockito) или `mvnw.cmd test`.

## Postman
Коллекция лежит в папке `../../../../../../postman`.

## Swagger UI
![Swagger](docs/swagger.png)
![Login](docs/swagger-login.png)
![Create task](docs/swagger-create-task.png)