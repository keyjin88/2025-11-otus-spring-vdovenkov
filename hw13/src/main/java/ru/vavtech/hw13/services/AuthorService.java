package ru.vavtech.hw13.services;


import ru.vavtech.hw13.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
