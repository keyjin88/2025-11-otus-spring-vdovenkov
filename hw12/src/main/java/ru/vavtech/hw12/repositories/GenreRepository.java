package ru.vavtech.hw12.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw12.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
