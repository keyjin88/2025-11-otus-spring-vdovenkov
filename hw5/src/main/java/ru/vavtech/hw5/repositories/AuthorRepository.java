package ru.vavtech.hw5.repositories;


import ru.vavtech.hw5.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(long id);
}
