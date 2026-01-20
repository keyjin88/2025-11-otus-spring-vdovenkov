--liquibase formatted sql

--changeset AV:2026-01-13--01-create-tables
CREATE TABLE authors
(
    id        BIGINT AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    CONSTRAINT author_pk PRIMARY KEY (id)
);

CREATE TABLE genres
(
    id   BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

CREATE TABLE books
(
    id        BIGINT AUTO_INCREMENT,
    title     VARCHAR(255) NOT NULL,
    author_id BIGINT       NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT author_fk FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE books_genres
(
    book_id  BIGINT REFERENCES books (id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, genre_id)
);

--changeset AV:2026-01-13--02-populate-db-test context:test
INSERT INTO authors(full_name)
VALUES ('Author_1'),
       ('Author_2'),
       ('Author_3');

INSERT INTO genres(name)
VALUES ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6');

INSERT INTO books(title, author_id)
VALUES ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

INSERT INTO books_genres(book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);