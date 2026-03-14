package ru.vavtech.hw13.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw13.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
