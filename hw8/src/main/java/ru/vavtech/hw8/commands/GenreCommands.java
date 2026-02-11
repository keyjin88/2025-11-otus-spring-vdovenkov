package ru.vavtech.hw8.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;
import ru.vavtech.hw8.converters.GenreConverter;
import ru.vavtech.hw8.services.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @Command(name = "ag", description = "Показать все жанры", group = "Жанры")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
