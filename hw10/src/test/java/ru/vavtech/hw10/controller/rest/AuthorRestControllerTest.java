package ru.vavtech.hw10.controller.rest;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.vavtech.hw10.model.dto.AuthorDto;
import ru.vavtech.hw10.service.AuthorService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorRestController.class)
@DisplayName("Author REST controller should")
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthorService authorService;

    @Test
    @DisplayName("correctly return list of all authors")
    void shouldReturnCorrectAuthorsList() throws Exception {
        var authors = List.of(
            new AuthorDto(1L, "Author 1"),
            new AuthorDto(2L, "Author 2")
        );
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(authors)));
    }
} 