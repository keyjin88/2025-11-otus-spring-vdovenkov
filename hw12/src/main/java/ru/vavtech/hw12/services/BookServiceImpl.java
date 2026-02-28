package ru.vavtech.hw12.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw12.exceptions.NotFoundException;
import ru.vavtech.hw12.mapper.BookMapper;
import ru.vavtech.hw12.models.Author;
import ru.vavtech.hw12.models.Book;
import ru.vavtech.hw12.models.Genre;
import ru.vavtech.hw12.models.dto.BookDto;
import ru.vavtech.hw12.repositories.AuthorRepository;
import ru.vavtech.hw12.repositories.BookRepository;
import ru.vavtech.hw12.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto create(String title, long authorId, long genreId) {
        var book = new Book();
        book.setTitle(title);
        book.setAuthor(authorById(authorId));
        book.setGenre(genreById(genreId));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, long genreId) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
        book.setTitle(title);
        book.setAuthor(authorById(authorId));
        book.setGenre(genreById(genreId));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author authorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Genre genreById(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(genreId)));
    }
}