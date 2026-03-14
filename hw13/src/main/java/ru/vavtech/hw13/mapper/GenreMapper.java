package ru.vavtech.hw13.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw13.models.Genre;
import ru.vavtech.hw13.models.dto.GenreDto;

@Component
public class GenreMapper {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
} 