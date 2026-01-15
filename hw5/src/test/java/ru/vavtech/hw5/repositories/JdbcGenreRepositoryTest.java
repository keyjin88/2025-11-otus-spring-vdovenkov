package ru.vavtech.hw5.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.vavtech.hw5.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void returnGenreById() {
        assertThat(genreRepository.findAllByIds(Set.of(1L, 4L)))
                .containsExactlyElementsOf(List.of(
                        new Genre(1L, "Genre_1"),
                        new Genre(4L, "Genre_4")));
    }

    @Test
    void loadAllGenre() {
        var genres = IntStream.range(1, 7).mapToObj(i -> new Genre(i, "Genre_" + i)).toList();
        assertThat(genreRepository.findAll())
                .containsExactlyElementsOf(genres);
    }
}