package ru.vavtech.hw8.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.vavtech.hw8.models.Author;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void returnAuthorById() {
        var actualAuthor = authorRepository.findById("1");
        var expectedAuthor = mongoTemplate.findById("1", Author.class);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @Test
    void returnEmptyWhenNotFound() {
        assertThat(authorRepository.findById("not-exist"))
                .isEmpty();
    }

    @Test
    void loadAllAuthors() {
        var authors = IntStream.range(1, 4).mapToObj(i -> new Author(String.valueOf(i), "Author_" + i)).toList();
        assertThat(authorRepository.findAll())
                .containsExactlyElementsOf(authors);
    }
}
