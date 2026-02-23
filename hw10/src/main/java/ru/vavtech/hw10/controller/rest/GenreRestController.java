package ru.vavtech.hw10.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vavtech.hw10.model.dto.GenreDto;
import ru.vavtech.hw10.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }
} 