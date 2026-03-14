package ru.vavtech.hw13.repositories;

import ru.vavtech.hw13.models.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);
}
