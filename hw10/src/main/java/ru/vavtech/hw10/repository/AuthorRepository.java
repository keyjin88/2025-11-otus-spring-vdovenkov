package ru.vavtech.hw10.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw10.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
