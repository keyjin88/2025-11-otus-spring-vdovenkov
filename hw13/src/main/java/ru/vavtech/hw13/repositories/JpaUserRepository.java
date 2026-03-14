package ru.vavtech.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw13.models.User;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);
}
