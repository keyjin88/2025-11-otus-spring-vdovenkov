package ru.vavtech.hw8.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vavtech.hw8.models.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAllByIdIn(List<String> ids);
}
