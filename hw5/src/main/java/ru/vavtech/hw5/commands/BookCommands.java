package ru.vavtech.hw5.commands;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;
import ru.vavtech.hw5.converters.BookConverter;
import ru.vavtech.hw5.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection"})
@RequiredArgsConstructor
@Component
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(name = "ab", description = "Показать все книги", group = "Книги")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(name = "bbid", description = "Найти книгу по id", group = "Книги")
    public String findBookById(
            @Option(shortName = 'i', longName = "id", description = "Идентификатор книги", required = true) long id
    ) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Command(name = "bins", description = "Добавить книгу", group = "Книги")
    public String insertBook(
            @Option(shortName = 't', longName = "title", description = "Название книги", required = true) String title,
            @Option(shortName = 'a', longName = "authorId", description = "Id автора", required = true) long authorId,
            @Option(shortName = 'g', longName = "genresIds",
                    description = "Список id жанров", required = true) Set<Long> genresIds
    ) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @Command(name = "bupd", description = "Обновить книгу", group = "Книги")
    public String updateBook(
            @Option(shortName = 'i', longName = "id", description = "Id книги", required = true) long id,
            @Option(shortName = 't', longName = "title", description = "Название книги", required = true) String title,
            @Option(shortName = 'a', longName = "authorId", description = "Id автора", required = true) long authorId,
            @Option(shortName = 'g', longName = "genresIds",
                    description = "Список id жанров", required = true) Set<Long> genresIds
    ) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @Command(name = "bdel", description = "Удалить книгу по id", group = "Книги")
    public void deleteBook(
            @Option(shortName = 'i', longName = "id", description = "Id книги", required = true) long id
    ) {
        bookService.deleteById(id);
    }
}