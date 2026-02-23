# Домашнее задание

Использовать WebFlux

## Запуск приложения

1. Поднять MongoDB в Docker (с аутентификацией root/password):
   ```bash
   # Первый запуск или после ошибки "Unauthorized" — пересоздать volume:
   ./start-mongo.sh
   # Или вручную:
   docker-compose down -v && docker-compose up -d
   ```
   **Важно:** Пользователь root создаётся только при первом запуске на **пустом** volume.
   Если видите ошибку "Command delete requires authentication" — выполните `docker-compose down -v` и запустите снова.
2. Запустить приложение:
   ```bash
   mvn spring-boot:run
   ```

3. Открыть в браузере: http://localhost:8080

***Цель:***

Цель: разрабатывать Responsive и Resilent приложения на реактивном стеке Spring c помощью Spring Web Flux и Reactive Spring Data Repositories
Результат: приложение на реактивном стеке Spring

***Описание/Пошаговая инструкция выполнения домашнего задания:***

Требования:

1. Переписать REST эндпойнты на WebFlux. Опционально: вместо контроллеров использовать Functional Endpoints;
2. Использовать Spring Data Reactive MongoDb;
3. Опциональное усложнение: Использовать Spring Data R2DBC и H2. Проблема N+1 должна быть решена (например, через кастомные репозитории и R2dbcEntityOperations)
4. Использование block() и subscribe() условно допустимо только в миграциях и тестах;
5. Протестировать все эндпойнты с помощью @WebFluxTest и моков сервисов;
6. Без фанатизма)


Рекомендации:

Старайтесь избавиться от лишних архитектурных слоёв. Самый простой вариант - весь flow в контроллере.