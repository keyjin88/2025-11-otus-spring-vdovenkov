package ru.vavtech.hw7.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;
import ru.vavtech.hw7.converters.AuthorConverter;
import ru.vavtech.hw7.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @Command(name = "aa", description = "Показать всех авторов", group = "Авторы")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
