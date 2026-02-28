package ru.vavtech.hw12.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw12.models.Author;
import ru.vavtech.hw12.models.dto.AuthorDto;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
} 