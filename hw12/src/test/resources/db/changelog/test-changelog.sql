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

INSERT INTO comments(book_id, text)
VALUES (1, 'Comment #1 to book 1'),
       (1, 'Comment #2 to book 1'),
       (2, 'Comment #1 to book 2');