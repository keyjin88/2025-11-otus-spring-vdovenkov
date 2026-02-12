package ru.vavtech.hw10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw10.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
