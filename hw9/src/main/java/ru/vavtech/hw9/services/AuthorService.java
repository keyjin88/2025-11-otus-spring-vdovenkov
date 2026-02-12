package ru.vavtech.hw9.services;


import ru.vavtech.hw9.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
