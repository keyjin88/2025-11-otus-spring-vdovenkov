package ru.vavtech.hw8.repositories;


import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vavtech.hw8.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    @NonNull
    Optional<Book> findById(@NonNull String id);

    @NonNull
    List<Book> findAll();
}
