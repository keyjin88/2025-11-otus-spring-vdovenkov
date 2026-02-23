package ru.vavtech.hw11.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    private Book book;
    private Author author;
    private Genre genre;
    private CreateBookDTO createBookDTO;
    private UpdateBookDTO updateBookDTO;

    @BeforeEach
    void setUp() {
        author = new Author("1", "Test Author");
        genre = new Genre("1", "Test Genre");
        book = new Book("1", "Test Book", author, genre);
        createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setAuthorId("1");
        createBookDTO.setGenreId("1");
        updateBookDTO = new UpdateBookDTO();
        updateBookDTO.setTitle("Updated Book");
        updateBookDTO.setAuthorId("1");
        updateBookDTO.setGenreId("1");
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(books));

        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(1)
                .contains(book);
    }

    @Test
    void getBookById_ShouldReturnBook() {
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));

        webTestClient.get()
                .uri("/api/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateBookDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void deleteBook_ShouldReturnNoContent() {
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(bookRepository.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldReturnNotFound() {
        when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/books/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldReturnNotFound() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));
        when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateBookDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldReturnNotFound() {
        when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/1")
                .exchange()
                .expectStatus().isNotFound();
    }
} 