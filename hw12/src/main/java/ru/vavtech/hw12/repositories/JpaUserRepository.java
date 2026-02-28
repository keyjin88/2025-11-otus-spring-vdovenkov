package ru.vavtech.hw12.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw12.models.User;

import java.util.Optional;

/**
 * JPA-репозиторий для работы с пользователями.
 */
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);
}
