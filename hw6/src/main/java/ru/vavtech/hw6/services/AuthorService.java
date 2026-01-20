package ru.vavtech.hw6.services;


import ru.vavtech.hw6.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
