package ru.vavtech.hw5.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vavtech.hw5.models.Genre;
import ru.vavtech.hw5.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
