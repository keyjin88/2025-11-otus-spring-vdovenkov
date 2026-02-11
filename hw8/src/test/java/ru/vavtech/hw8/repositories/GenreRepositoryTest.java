package ru.vavtech.hw8.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.vavtech.hw8.models.Genre;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void returnGenreById() {
        var actualGenre = genreRepository.findById("1");
        var expectedGenre = mongoTemplate.findById("1", Genre.class);
        assertThat(actualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @Test
    void loadAllGenre() {
        var genres = IntStream.range(1, 4).mapToObj(i -> new Genre(String.valueOf(i), "Genre_" + i)).toList();
        assertThat(genreRepository.findAll())
                .containsExactlyElementsOf(genres);
    }
}
