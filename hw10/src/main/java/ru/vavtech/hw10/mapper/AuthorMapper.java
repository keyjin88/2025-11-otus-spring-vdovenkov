package ru.vavtech.hw10.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw10.model.Author;
import ru.vavtech.hw10.model.dto.AuthorDto;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
} 