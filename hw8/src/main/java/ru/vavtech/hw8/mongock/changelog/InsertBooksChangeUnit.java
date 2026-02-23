package ru.vavtech.hw8.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import ru.vavtech.hw8.models.Book;
import ru.vavtech.hw8.repositories.AuthorRepository;
import ru.vavtech.hw8.repositories.BookRepository;
import ru.vavtech.hw8.repositories.GenreRepository;

import java.util.List;

@ChangeUnit(id = "insertBooks", order = "003", author = "nrr")
public class InsertBooksChangeUnit {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public InsertBooksChangeUnit(BookRepository bookRepository,
                                 AuthorRepository authorRepository,
                                 GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Execution
    public void execute() {
        Book book = new Book("1", "Book_1", authorRepository.findById("2").orElseThrow(),
                List.of(genreRepository.findById("2").orElseThrow(), genreRepository.findById("1").orElseThrow()));
        Book book2 = new Book("2", "Book_2", authorRepository.findById("1").orElseThrow(),
                List.of(genreRepository.findById("3").orElseThrow(), genreRepository.findById("2").orElseThrow()));
        Book book3 = new Book("3", "Book_3", authorRepository.findById("1").orElseThrow(),
                List.of(genreRepository.findById("1").orElseThrow()));
        Book book4 = new Book("4", "Book_4", authorRepository.findById("3").orElseThrow(),
                List.of(genreRepository.findById("3").orElseThrow(), genreRepository.findById("1").orElseThrow()));

        bookRepository.insert(List.of(book, book2, book3, book4));
    }

    @RollbackExecution
    public void rollback() {
        bookRepository.deleteAll();
    }
}
