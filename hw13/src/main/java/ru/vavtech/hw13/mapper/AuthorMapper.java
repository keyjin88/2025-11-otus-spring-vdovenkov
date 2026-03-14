package ru.vavtech.hw13.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw13.models.Author;
import ru.vavtech.hw13.models.dto.AuthorDto;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
} 