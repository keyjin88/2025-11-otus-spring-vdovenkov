package ru.vavtech.hw7.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw7.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
//    List<Author> findAll();
//
//    Optional<Author> findById(long id);
}
