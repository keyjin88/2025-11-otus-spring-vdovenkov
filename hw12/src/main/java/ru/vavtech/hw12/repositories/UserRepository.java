package ru.vavtech.hw12.repositories;

import ru.vavtech.hw12.models.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository {

    Optional<User> findByUsername(String username);
}
