package ru.vavtech.hw6.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.vavtech.hw6.models.Author;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void returnAuthorById() {
        var actualAuthor = authorRepository.findById(1);
        var expectedAuthor = em.find(Author.class, 1);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @Test
    void returnEmptyWhenNotFound() {
        assertThat(authorRepository.findById(-1))
                .isEmpty();
    }

    @Test
    void loadAllAuthors() {
        var authors = IntStream.range(1, 4).mapToObj(i -> new Author(i, "Author_" + i)).toList();
        assertThat(authorRepository.findAll())
                .containsExactlyElementsOf(authors);
    }
}
