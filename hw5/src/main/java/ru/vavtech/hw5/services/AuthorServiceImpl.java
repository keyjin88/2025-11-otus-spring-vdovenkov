package ru.vavtech.hw5.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vavtech.hw5.models.Author;
import ru.vavtech.hw5.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
