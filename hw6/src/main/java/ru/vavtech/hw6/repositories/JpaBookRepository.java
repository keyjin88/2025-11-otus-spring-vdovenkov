package ru.vavtech.hw6.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.vavtech.hw6.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        var graph = entityManager.getEntityGraph("books_with_author_and_genre");
        return entityManager.createQuery(
                "FROM Book b ", Book.class)
                .setHint(FETCH.getKey(), graph)
                .getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        var graph = entityManager.getEntityGraph("books_with_author_and_genre");
        var books = entityManager.createQuery(
                "FROM Book b " +
                "WHERE b.id = :book_id", Book.class)
                .setParameter("book_id", id)
                .setHint(FETCH.getKey(), graph)
                .getResultList();
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var comments = entityManager.createQuery(
                        "FROM Comment c WHERE c.book.id = :book_id",
                        ru.vavtech.hw6.models.Comment.class)
                .setParameter("book_id", id)
                .getResultList();
        comments.forEach(entityManager::remove);

        var book = entityManager.find(Book.class, id);
        if (book == null) {
            return;
        }
        entityManager.remove(book);
    }
}
