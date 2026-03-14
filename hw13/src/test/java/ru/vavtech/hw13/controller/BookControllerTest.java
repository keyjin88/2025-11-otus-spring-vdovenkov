package ru.vavtech.hw13.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.vavtech.hw13.models.Author;
import ru.vavtech.hw13.models.Genre;
import ru.vavtech.hw13.models.dto.AuthorDto;
import ru.vavtech.hw13.models.dto.BookDto;
import ru.vavtech.hw13.models.dto.GenreDto;
import ru.vavtech.hw13.models.dto.UpdateBookDto;
import ru.vavtech.hw13.services.AuthorService;
import ru.vavtech.hw13.services.BookService;
import ru.vavtech.hw13.services.CommentService;
import ru.vavtech.hw13.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Book controller test")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private CommentService commentService;

    private static AuthorDto getAuthorDto() {
        return new AuthorDto(1L, "Test Author");
    }

    private static Author getAuthor() {
        return new Author(1L, "Test Author");
    }

    private static GenreDto getGenreDto() {
        return new GenreDto(1L, "Test Genre");
    }

    private static Genre getGenre() {
        return new Genre(1L, "Test Genre");
    }

    private static BookDto getBookDto() {
        return new BookDto(1L, "Test Book", getAuthorDto(), getGenreDto());
    }

    private static UpdateBookDto getUpdateBookDto() {
        return new UpdateBookDto(1L, "Test Book", 1L, 1L);
    }

    @DisplayName("Should correctly return list of all books")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var books = List.of(getBookDto());
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books-list"))
                .andExpect(model().attribute("books", books));
    }

    @DisplayName("Should correctly return add book page")
    @Test
    void shouldReturnCorrectAddBookPage() throws Exception {
        var authors = List.of(getAuthor());
        var genres = List.of(getGenre());
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().attribute("book", new UpdateBookDto()))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("Should correctly add new book")
    @Test
    void shouldCorrectlyAddNewBook() throws Exception {
        mvc.perform(post("/add")
                        .param("title", "New Book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).create("New Book", 1L, 1L);
    }

    @DisplayName("Should show validation errors when adding book with invalid data")
    @Test
    void shouldShowValidationErrorsWhenAddingInvalidBook() throws Exception {
        var authors = List.of(getAuthor());
        var genres = List.of(getGenre());
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(post("/add")
                        .param("title", "")
                        .param("authorId", "")
                        .param("genreId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("book", "title", "authorId", "genreId"));
    }

    @DisplayName("Should correctly return edit book page")
    @Test
    void shouldReturnCorrectEditBookPage() throws Exception {
        var book = getBookDto();
        var updateBookDto = getUpdateBookDto();
        var authors = List.of(getAuthor());
        var genres = List.of(getGenre());
        given(bookService.findById(1L)).willReturn(book);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", updateBookDto))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("Should correctly delete book")
    @Test
    void shouldCorrectlyDeleteBook() throws Exception {
        mvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).deleteById(1L);
    }

    @DisplayName("Should correctly update book")
    @Test
    void shouldCorrectlyUpdateBook() throws Exception {
        mvc.perform(post("/update/1")
                        .param("title", "Updated Book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).update(1L, "Updated Book", 1L, 1L);
    }

    @DisplayName("Should show validation errors when updating book with invalid data")
    @Test
    void shouldShowValidationErrorsWhenUpdatingInvalidBook() throws Exception {
        var authors = List.of(getAuthor());
        var genres = List.of(getGenre());
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(post("/update/1")
                        .param("title", "")
                        .param("authorId", "")
                        .param("genreId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("book", "title", "authorId", "genreId"));
    }
} 