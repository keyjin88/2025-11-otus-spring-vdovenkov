package ru.vavtech.hw6.converters;

import org.springframework.stereotype.Component;
import ru.vavtech.hw6.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
