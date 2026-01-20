package ru.vavtech.hw6.converters;

import org.springframework.stereotype.Component;
import ru.vavtech.hw6.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
