package ru.vavtech.hw7.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw7.models.Genre;
import ru.vavtech.hw7.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
