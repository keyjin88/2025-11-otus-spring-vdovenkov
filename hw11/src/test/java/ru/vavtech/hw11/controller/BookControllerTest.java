package ru.vavtech.hw11.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import ru.vavtech.hw11.repository.BookRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Disabled
@WebFluxTest(BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookRepository bookRepository;

    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        author = new Author("1", "Test Author");
        genre = new Genre("1", "Test Genre");
        book = new Book("1", "Test Book", author, genre);
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
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
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
        when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
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