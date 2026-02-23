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
import ru.vavtech.hw11.repository.AuthorRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("1", "Test Author");
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() {
        List<Author> authors = Arrays.asList(author);
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authors));

        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class)
                .hasSize(1)
                .contains(author);
    }

    @Test
    void getAuthorById_ShouldReturnAuthor() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));

        webTestClient.get()
                .uri("/api/authors/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .isEqualTo(author);
    }

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() {
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));

        webTestClient.post()
                .uri("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(author)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Author.class)
                .isEqualTo(author);
    }

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));

        webTestClient.put()
                .uri("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(author)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .isEqualTo(author);
    }

    @Test
    void deleteAuthor_ShouldReturnNoContent() {
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(authorRepository.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/authors/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getAuthorById_WhenAuthorNotFound_ShouldReturnNotFound() {
        when(authorRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/authors/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateAuthor_WhenAuthorNotFound_ShouldReturnNotFound() {
        when(authorRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(author)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteAuthor_WhenAuthorNotFound_ShouldReturnNotFound() {
        when(authorRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/authors/1")
                .exchange()
                .expectStatus().isNotFound();
    }
} 