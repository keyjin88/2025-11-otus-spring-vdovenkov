package ru.vavtech.hw7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw7.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
