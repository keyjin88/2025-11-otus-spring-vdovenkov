package ru.vavtech.hw9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw9.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
