package ru.vavtech.hw11.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vavtech.hw11.model.Author;
import ru.vavtech.hw11.model.Book;
import ru.vavtech.hw11.model.Genre;
import ru.vavtech.hw11.repository.AuthorRepository;
import ru.vavtech.hw11.repository.BookRepository;
import ru.vavtech.hw11.repository.GenreRepository;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(AuthorRepository authorRepository,
                                          GenreRepository genreRepository,
                                          BookRepository bookRepository) {
        return args -> {
            clearDatabase(authorRepository, genreRepository, bookRepository);
            initializeAuthorsAndGenres(authorRepository, genreRepository);
            initializeBooks(bookRepository);
        };
    }

    private void clearDatabase(AuthorRepository authorRepository,
                               GenreRepository genreRepository,
                               BookRepository bookRepository) {
        Mono.when(
                authorRepository.deleteAll(),
                genreRepository.deleteAll(),
                bookRepository.deleteAll()
        ).block();
    }

    private void initializeAuthorsAndGenres(AuthorRepository authorRepository,
                                            GenreRepository genreRepository) {
        var tolstoy = new Author("1", "Лев Толстой");
        var dostoevsky = new Author("2", "Федор Достоевский");

        var novel = new Genre("1", "Роман");
        var fantasy = new Genre("2", "Фантастика");

        Mono.when(
            authorRepository.saveAll(Flux.just(tolstoy, dostoevsky)),
            genreRepository.saveAll(Flux.just(novel, fantasy))
        ).block();
    }

    private void initializeBooks(BookRepository bookRepository) {
        var tolstoy = new Author("1", "Лев Толстой");
        var dostoevsky = new Author("2", "Федор Достоевский");

        var novel = new Genre("1", "Роман");

        Flux.just(
                new Book("1", "Война и мир", tolstoy, novel),
                new Book("2", "Преступление и наказание", dostoevsky, novel)
        ).flatMap(bookRepository::save).collectList().block();
    }
} 