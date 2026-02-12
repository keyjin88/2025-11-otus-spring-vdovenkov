package ru.vavtech.hw10.service;

import ru.vavtech.hw10.model.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
