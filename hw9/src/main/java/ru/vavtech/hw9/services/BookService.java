package ru.vavtech.hw9.services;


import ru.vavtech.hw9.models.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(String title, long authorId, long genreId);

    BookDto update(long id, String title, long authorId, long genreId);

    void deleteById(long id);
}
