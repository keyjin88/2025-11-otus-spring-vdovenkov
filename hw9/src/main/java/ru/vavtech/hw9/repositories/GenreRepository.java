package ru.vavtech.hw9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw9.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
