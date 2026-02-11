package ru.vavtech.hw8.converters;

import org.springframework.stereotype.Component;
import ru.vavtech.hw8.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
