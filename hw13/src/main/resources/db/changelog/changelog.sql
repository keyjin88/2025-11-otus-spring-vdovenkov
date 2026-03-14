--liquibase formatted sql

--changeset AV:2026-01-13--01-create-tables
CREATE TABLE authors
(
    id        BIGINT AUTO_INCREMENT,
    full_name VARCHAR(255),
    CONSTRAINT author_pk PRIMARY KEY (id)
);

CREATE TABLE genres
(
    id   BIGINT AUTO_INCREMENT,
    name VARCHAR(255),
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

CREATE TABLE books
(
    id        BIGINT AUTO_INCREMENT,
    title     VARCHAR(255),
    author_id BIGINT NOT NULL,
    genre_id  BIGINT NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT author_fk FOREIGN KEY (author_id) REFERENCES authors (id),
    CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genres (id)
);

CREATE TABLE comments
(
    id      BIGINT AUTO_INCREMENT,
    text    VARCHAR(255),
    book_id BIGINT NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (id),
    CONSTRAINT book_fk FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

--changeset AV:2026-02-28--01-create-users
CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_username_unique UNIQUE (username)
);

--changeset AV:2026-01-13--02-populate-db context:!test
INSERT INTO authors(full_name)
VALUES ('Lewis Carroll'),
       ('Alexandre Dumas'),
       ('Leo Tolstoy');

INSERT INTO genres(name)
VALUES ('Fantasy'),
       ('Adventure'),
       ('Romance');

INSERT INTO books(title, author_id, genre_id)
VALUES ('Alice’s Adventures in Wonderland', 1, 1),
       ('The Three Musketeers', 2, 2),
       ('Anna Karenina', 3, 3);

INSERT INTO comments(text, book_id)
VALUES ('Comment #1 to book 1', 1),
       ('Comment #2 to book 1', 1),
       ('Comment #3 to book 1', 1),
       ('Comment #1 to book 2', 2);

-- Пользователи: admin/password (ADMIN), user/password (USER). Пароли закодированы BCrypt.
INSERT INTO users(username, password, role)
VALUES ('admin', '$2a$10$VPsdXQSZHaCm7O/kwAVZrOr9ES2HCWN0OnfA/bI3r8m5Dvil5xyIq', 'ROLE_ADMIN'),
       ('user', '$2a$10$VPsdXQSZHaCm7O/kwAVZrOr9ES2HCWN0OnfA/bI3r8m5Dvil5xyIq', 'ROLE_USER');

--changeset AV:2026-03-14--01-add-comment-owner
ALTER TABLE comments ADD COLUMN user_id BIGINT NULL;
ALTER TABLE comments ADD CONSTRAINT comment_user_fk FOREIGN KEY (user_id) REFERENCES users (id);