package ru.vavtech.hw8.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.vavtech.hw8.models.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import(MongoTestDataInitializer.class)
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAllTest() {

        List<Book> bookList = bookRepository.findAll();
        List<Book> expectedBookList = mongoTemplate.findAll(Book.class);

        assertThat(bookList).usingRecursiveComparison().isEqualTo(expectedBookList);

    }

    @Test
    void findByIdTest() {
        Optional<Book> book = bookRepository.findById("1");
        Book expectedBook = mongoTemplate.findById("1", Book.class);

        assertThat(book).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

}