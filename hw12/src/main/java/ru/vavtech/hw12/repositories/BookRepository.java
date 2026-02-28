package ru.vavtech.hw12.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw12.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph("books_with_author_and_genre")
    Optional<Book> findById(long id);

    @Override
    @EntityGraph("books_with_author_and_genre")
    List<Book> findAll();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("delete from Book b where b.id = :id")
    void deleteById(@Param("id") long id);
}
