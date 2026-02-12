package ru.vavtech.hw10.controller.rest;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.vavtech.hw10.model.dto.GenreDto;
import ru.vavtech.hw10.service.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
@DisplayName("Genre REST controller should")
class GenreRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("correctly return list of all genres")
    void shouldReturnCorrectGenresList() throws Exception {
        var genres = List.of(
            new GenreDto(1L, "Genre 1"),
            new GenreDto(2L, "Genre 2")
        );
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(genres)));
    }
} 