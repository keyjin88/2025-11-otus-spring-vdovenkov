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
import ru.vavtech.hw11.model.Genre;
import ru.vavtech.hw11.repository.GenreRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GenreRepository genreRepository;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = new Genre("1", "Test Genre");
    }

    @Test
    void getAllGenres_ShouldReturnListOfGenres() {
        List<Genre> genres = Arrays.asList(genre);
        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(genres));

        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Genre.class)
                .hasSize(1)
                .contains(genre);
    }

    @Test
    void getGenreById_ShouldReturnGenre() {
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));

        webTestClient.get()
                .uri("/api/genres/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Genre.class)
                .isEqualTo(genre);
    }

    @Test
    void createGenre_ShouldReturnCreatedGenre() {
        when(genreRepository.save(any(Genre.class))).thenReturn(Mono.just(genre));

        webTestClient.post()
                .uri("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genre)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Genre.class)
                .isEqualTo(genre);
    }

    @Test
    void updateGenre_ShouldReturnUpdatedGenre() {
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));
        when(genreRepository.save(any(Genre.class))).thenReturn(Mono.just(genre));

        webTestClient.put()
                .uri("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genre)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Genre.class)
                .isEqualTo(genre);
    }

    @Test
    void deleteGenre_ShouldReturnNoContent() {
        when(genreRepository.findById("1")).thenReturn(Mono.just(genre));
        when(genreRepository.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/genres/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getGenreById_WhenGenreNotFound_ShouldReturnNotFound() {
        when(genreRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/genres/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateGenre_WhenGenreNotFound_ShouldReturnNotFound() {
        when(genreRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genre)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteGenre_WhenGenreNotFound_ShouldReturnNotFound() {
        when(genreRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/genres/1")
                .exchange()
                .expectStatus().isNotFound();
    }
} 