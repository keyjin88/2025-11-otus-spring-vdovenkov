package ru.vavtech.hw12.services;


import ru.vavtech.hw12.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
