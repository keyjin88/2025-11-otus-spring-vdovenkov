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
import ru.vavtech.hw11.model.Comment;
import ru.vavtech.hw11.model.Genre;
import ru.vavtech.hw11.repository.CommentRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CommentRepository commentRepository;

    private Comment comment;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        author = new Author("1", "Test Author");
        genre = new Genre("1", "Test Genre");
        book = new Book("1", "Test Book", author, genre);
        comment = new Comment("1", "Test Comment", book);
    }

    @Test
    void getAllComments_ShouldReturnListOfComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findAll()).thenReturn(Flux.fromIterable(comments));

        webTestClient.get()
                .uri("/api/comments")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Comment.class)
                .hasSize(1)
                .contains(comment);
    }

    @Test
    void getCommentById_ShouldReturnComment() {
        when(commentRepository.findById("1")).thenReturn(Mono.just(comment));

        webTestClient.get()
                .uri("/api/comments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);
    }

    @Test
    void createComment_ShouldReturnCreatedComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(Mono.just(comment));

        webTestClient.post()
                .uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(comment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Comment.class)
                .isEqualTo(comment);
    }

    @Test
    void updateComment_ShouldReturnUpdatedComment() {
        when(commentRepository.findById("1")).thenReturn(Mono.just(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(Mono.just(comment));

        webTestClient.put()
                .uri("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(comment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);
    }

    @Test
    void deleteComment_ShouldReturnNoContent() {
        when(commentRepository.findById("1")).thenReturn(Mono.just(comment));
        when(commentRepository.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/comments/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getCommentById_WhenCommentNotFound_ShouldReturnNotFound() {
        when(commentRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/comments/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateComment_WhenCommentNotFound_ShouldReturnNotFound() {
        when(commentRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(comment)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteComment_WhenCommentNotFound_ShouldReturnNotFound() {
        when(commentRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/comments/1")
                .exchange()
                .expectStatus().isNotFound();
    }
} 