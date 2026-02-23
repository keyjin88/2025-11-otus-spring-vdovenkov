package ru.vavtech.hw10.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw10.model.Genre;
import ru.vavtech.hw10.model.dto.GenreDto;

@Component
public class GenreMapper {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
} 