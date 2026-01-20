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

--changeset AV:2026-01-13--02-populate-db context:dev
INSERT INTO authors(full_name)
VALUES ('Lewis Carroll'),
       ('Alexandre Dumas'),
       ('Leo Tolstoy');

INSERT INTO genres(name)
VALUES ('Fantasy'),
       ('Adventure'),
       ('Romance');

INSERT INTO books(title, author_id)
VALUES ('Alice’s Adventures in Wonderland', 1),
       ('The Three Musketeers', 2),
       ('Anna Karenina', 3);

INSERT INTO books_genres (book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (2, 3),
       (3, 3);