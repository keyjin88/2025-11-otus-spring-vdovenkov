package ru.vavtech.hw12.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw12.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
