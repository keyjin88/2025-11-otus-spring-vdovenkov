--liquibase formatted sql

--changeset AV:2026-01-13--02-populate-db-test context:test
INSERT INTO authors(full_name)
VALUES ('Author_1'),
       ('Author_2'),
       ('Author_3');

INSERT INTO genres(name)
VALUES ('Genre_1'),
       ('Genre_2'),
       ('Genre_3');

INSERT INTO books(title, author_id, genre_id)
VALUES ('Book_1', 1, 1),
       ('Book_2', 2, 2),
       ('Book_3', 3, 3);

-- Пользователи для тестов: admin/password (ADMIN), testuser/password (USER). Вставляем до comments из-за FK.
INSERT INTO users(username, password, role)
VALUES ('admin', '$2a$10$VPsdXQSZHaCm7O/kwAVZrOr9ES2HCWN0OnfA/bI3r8m5Dvil5xyIq', 'ROLE_ADMIN'),
       ('testuser', '$2a$10$VPsdXQSZHaCm7O/kwAVZrOr9ES2HCWN0OnfA/bI3r8m5Dvil5xyIq', 'ROLE_USER');

INSERT INTO comments(book_id, text, user_id)
VALUES (1, 'Comment #1 to book 1', 1),
       (1, 'Comment #2 to book 1', 1),
       (2, 'Comment #1 to book 2', 1);