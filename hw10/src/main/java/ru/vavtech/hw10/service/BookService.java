package ru.vavtech.hw10.service;

import ru.vavtech.hw10.model.dto.BookDto;
import ru.vavtech.hw10.model.dto.CreateBookDto;
import ru.vavtech.hw10.model.dto.UpdateBookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(CreateBookDto createBookDto);

    BookDto update(UpdateBookDto updateBookDto);

    void deleteById(long id);
}
