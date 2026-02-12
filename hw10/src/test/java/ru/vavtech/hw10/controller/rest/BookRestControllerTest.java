package ru.vavtech.hw10.controller.rest;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.vavtech.hw10.model.dto.AuthorDto;
import ru.vavtech.hw10.model.dto.BookDto;
import ru.vavtech.hw10.model.dto.CreateBookDto;
import ru.vavtech.hw10.model.dto.GenreDto;
import ru.vavtech.hw10.model.dto.UpdateBookDto;
import ru.vavtech.hw10.service.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@DisplayName("Book REST controller should")
class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @Test
    @DisplayName("correctly return list of all books")
    void shouldReturnCorrectBooksList() throws Exception {
        var author1 = new AuthorDto(1L, "Author 1");
        var author2 = new AuthorDto(2L, "Author 2");
        var genre1 = new GenreDto(1L, "Genre 1");
        var genre2 = new GenreDto(2L, "Genre 2");
        
        var books = List.of(
            new BookDto(1L, "Book 1", author1, genre1),
            new BookDto(2L, "Book 2", author2, genre2)
        );
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("correctly return book by id")
    void shouldReturnCorrectBook() throws Exception {
        var author = new AuthorDto(1L, "Author 1");
        var genre = new GenreDto(1L, "Genre 1");
        var expectedBook = new BookDto(1L, "Book 1", author, genre);
        given(bookService.findById(1L)).willReturn(expectedBook);

        mvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @Test
    @DisplayName("correctly create book")
    void shouldCorrectlyCreateBook() throws Exception {
        var bookToCreate = new CreateBookDto();
        bookToCreate.setTitle("New Book");
        bookToCreate.setAuthorId(1L);
        bookToCreate.setGenreId(1L);
        var author = new AuthorDto(1L, "Author 1");
        var genre = new GenreDto(1L, "Genre 1");
        var createdBook = new BookDto(1L, "New Book", author, genre);

        given(bookService.create(eq(bookToCreate)))
            .willReturn(createdBook);

        mvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookToCreate)))
            .andExpect(status().isCreated())
            .andExpect(content().json(mapper.writeValueAsString(createdBook)));
    }

    @Test
    @DisplayName("correctly update book")
    void shouldCorrectlyUpdateBook() throws Exception {
        var bookToUpdate = new UpdateBookDto(1L, "Updated Book", 1L, 1L);
        var author = new AuthorDto(1L, "Author 1");
        var genre = new GenreDto(1L, "Genre 1");
        var updatedBook = new BookDto(1L, "Updated Book", author, genre);

        given(bookService.update(eq(bookToUpdate)))
            .willReturn(updatedBook);

        mvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookToUpdate)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(updatedBook)));
    }

    @Test
    @DisplayName("correctly delete book")
    void shouldCorrectlyDeleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("return 400 when creating book with invalid data")
    void shouldReturn400WhenCreatingBookWithInvalidData() throws Exception {
        var invalidBook = new CreateBookDto();
        invalidBook.setTitle("");
        invalidBook.setAuthorId(null);
        invalidBook.setGenreId(null);

        mvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidBook)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("return 404 when book not found by id")
    void shouldReturn404WhenBookNotFound() throws Exception {
        given(bookService.findById(999L)).willReturn(null);

        mvc.perform(get("/api/books/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("return 500 when internal server error occurs")
    void shouldReturn500WhenInternalServerError() throws Exception {
        given(bookService.findById(1L)).willThrow(new RuntimeException("Internal Server Error"));

        mvc.perform(get("/api/books/1"))
            .andExpect(status().isInternalServerError());
    }
} 