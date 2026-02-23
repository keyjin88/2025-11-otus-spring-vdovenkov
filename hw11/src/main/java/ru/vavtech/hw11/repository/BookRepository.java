package ru.vavtech.hw11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.vavtech.hw11.model.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
