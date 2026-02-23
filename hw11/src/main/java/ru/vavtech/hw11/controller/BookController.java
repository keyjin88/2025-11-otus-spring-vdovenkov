package ru.vavtech.hw11.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vavtech.hw11.model.Author;
import ru.vavtech.hw11.model.Book;
import ru.vavtech.hw11.model.Genre;
import ru.vavtech.hw11.model.dto.CreateBookDTO;
import ru.vavtech.hw11.model.dto.UpdateBookDTO;
import ru.vavtech.hw11.repository.AuthorRepository;
import ru.vavtech.hw11.repository.BookRepository;
import ru.vavtech.hw11.repository.GenreRepository;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable String id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Book>> createBook(@RequestBody CreateBookDTO createBookDTO) {
        log.info("Received book data: title={}, authorId={}, genreId={}",
                createBookDTO.getTitle(), createBookDTO.getAuthorId(), createBookDTO.getGenreId());

        return Mono.zip(
                authorRepository.findById(createBookDTO.getAuthorId()),
                genreRepository.findById(createBookDTO.getGenreId())
        ).flatMap(tuple -> {
            Author author = tuple.getT1();
            Genre genre = tuple.getT2();
            Book book = new Book();
            book.setTitle(createBookDTO.getTitle());
            book.setAuthor(author);
            book.setGenre(genre);
            return bookRepository.save(book);
        }).map(savedBook -> {
            log.info("Saved book: {}", savedBook);
            return ResponseEntity.status(201).body(savedBook);
        }).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Book>> updateBook(@PathVariable String id, @RequestBody UpdateBookDTO updateBookDTO) {
        return Mono.zip(
                authorRepository.findById(updateBookDTO.getAuthorId()),
                genreRepository.findById(updateBookDTO.getGenreId())
        ).flatMap(tuple -> {
            Author author = tuple.getT1();
            Genre genre = tuple.getT2();
            return bookRepository.findById(id)
                    .flatMap(existingBook -> {
                        existingBook.setTitle(updateBookDTO.getTitle());
                        existingBook.setAuthor(author);
                        existingBook.setGenre(genre);
                        return bookRepository.save(existingBook);
                    })
                    .map(ResponseEntity::ok);
        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable String id) {
        return bookRepository.findById(id)
                .flatMap(book -> bookRepository.deleteById(id)
                        .then(Mono.just(ResponseEntity.noContent().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
} 