package ru.vavtech.hw10.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw10.exception.NotFoundException;
import ru.vavtech.hw10.mapper.BookMapper;
import ru.vavtech.hw10.model.Author;
import ru.vavtech.hw10.model.Book;
import ru.vavtech.hw10.model.Genre;
import ru.vavtech.hw10.model.dto.BookDto;
import ru.vavtech.hw10.model.dto.CreateBookDto;
import ru.vavtech.hw10.model.dto.UpdateBookDto;
import ru.vavtech.hw10.repository.AuthorRepository;
import ru.vavtech.hw10.repository.BookRepository;
import ru.vavtech.hw10.repository.GenreRepository;

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
    public BookDto create(CreateBookDto createBookDto) {
        var book = new Book();
        book.setTitle(createBookDto.getTitle());
        book.setAuthor(authorById(createBookDto.getAuthorId()));
        book.setGenre(genreById(createBookDto.getGenreId()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(UpdateBookDto updateBookDto) {
        var book = bookRepository.findById(updateBookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(updateBookDto.getId())));
        book.setTitle(updateBookDto.getTitle());
        book.setAuthor(authorById(updateBookDto.getAuthorId()));
        book.setGenre(genreById(updateBookDto.getGenreId()));
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