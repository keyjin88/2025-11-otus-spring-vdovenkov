package ru.vavtech.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw13.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
