package ru.vavtech.hw10.service;

import ru.vavtech.hw10.model.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
