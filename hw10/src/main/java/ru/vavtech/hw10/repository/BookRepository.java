package ru.vavtech.hw10.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw10.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph("books_with_author_and_genre")
    Optional<Book> findById(long id);

    @Override
    @EntityGraph("books_with_author_and_genre")
    List<Book> findAll();
}
