package ru.vavtech.hw5.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.vavtech.hw5.models.Author;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void returnAuthorById() {
        assertThat(authorRepository.findById(1))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new Author(1, "Author_1"));
    }

    @Test
    void loadAllAuthors() {
        var authors = IntStream.range(1, 4).mapToObj(i -> new Author(i, "Author_" + i)).toList();
        assertThat(authorRepository.findAll())
                .containsExactlyElementsOf(authors);
    }
}