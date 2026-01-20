package ru.vavtech.hw6.commands;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.vavtech.hw6.converters.BookConverter;
import ru.vavtech.hw6.services.BookService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @ShellMethod(key = "bins", value = "Insert book")
    public String createBook(String title, long authorId, long genreId) {
        var savedBook = bookService.create(title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(key = "bupd", value = "Update book")
    public String updateBook(long id, String title, long authorId, long genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(key = "bdel", value = "Delete book by id")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}