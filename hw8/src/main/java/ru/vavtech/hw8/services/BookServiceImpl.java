package ru.vavtech.hw8.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw8.exceptions.EntityNotFoundException;
import ru.vavtech.hw8.models.Book;
import ru.vavtech.hw8.repositories.AuthorRepository;
import ru.vavtech.hw8.repositories.BookRepository;
import ru.vavtech.hw8.repositories.CommentRepository;
import ru.vavtech.hw8.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(String title, String authorId, List<String> genreIds) {
        Book book = new Book();
        if ("".equals(title)) {
            throw new IllegalArgumentException("Book title can't be empty");
        }
        if ("".equals(authorId)) {
            throw new IllegalArgumentException("Book author id can't be empty");
        }
        if (genreIds == null || genreIds.isEmpty()) {
            throw new IllegalArgumentException("Book genres ids can't be empty");
        }
        book.setTitle(title);
        book.setAuthor(authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId))));
        book.setGenres(genreRepository.findAllByIdIn(genreIds));
        return bookRepository.save(book);
    }

    @Override
    public Book update(String id, String title, String authorId, List<String> genreIds) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        if (!"".equals(title)) {
            book.setTitle(title);
        }
        if (authorId != null) {
            book.setAuthor(authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId))));
        }
        if (genreIds != null) {
            book.setGenres(genreRepository.findAllByIdIn(genreIds));
        }
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
}