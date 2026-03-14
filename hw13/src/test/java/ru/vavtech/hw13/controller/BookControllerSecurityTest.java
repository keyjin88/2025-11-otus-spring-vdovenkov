package ru.vavtech.hw13.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.vavtech.hw13.configuration.SecurityConfig;
import ru.vavtech.hw13.security.UserDetailsServiceImpl;
import ru.vavtech.hw13.services.AuthorService;
import ru.vavtech.hw13.services.BookService;
import ru.vavtech.hw13.services.GenreService;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Тесты защиты ресурсов")
@WebMvcTest(controllers = {BookController.class, LoginController.class, CommentController.class})
@Import(SecurityConfig.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private ru.vavtech.hw13.services.CommentService commentService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        given(bookService.findById(1L)).willReturn(new ru.vavtech.hw13.models.dto.BookDto(1L, "Test",
                new ru.vavtech.hw13.models.dto.AuthorDto(1L, "Author"),
                new ru.vavtech.hw13.models.dto.GenreDto(1L, "Genre")));
        given(commentService.findByBookId(1L)).willReturn(java.util.List.of());
    }

    @DisplayName("Неаутентифицированный GET перенаправляет на логин")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"/, главная страница", "/add, страница добавления книги", "/edit/1, страница редактирования книги", "/book/1, страница книги"})
    void unauthenticatedGetRedirectsToLogin(String url, String description) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @DisplayName("Неаутентифицированный POST перенаправляет на логин")
    @ParameterizedTest(name = "{0}")
    @MethodSource("unauthenticatedPostRequests")
    void unauthenticatedPostRedirectsToLogin(String description, MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    static Stream<Arguments> unauthenticatedPostRequests() {
        return Stream.of(
                Arguments.arguments(
                        "добавление книги",
                        post("/add").with(csrf())
                                .param("title", "New Book")
                                .param("authorId", "1")
                                .param("genreId", "1")
                ),
                Arguments.arguments(
                        "удаление книги",
                        post("/delete/1").with(csrf())
                ),
                Arguments.arguments(
                        "добавление комментария",
                        post("/book/1/comment").with(csrf()).param("text", "Новый комментарий")
                ),
                Arguments.arguments(
                        "удаление комментария",
                        post("/comment/1/delete").with(csrf()).param("bookId", "1")
                ),
                Arguments.arguments(
                        "обновление комментария",
                        post("/comment/1/update").with(csrf()).param("text", "Обновлённый текст")
                )
        );
    }

    @DisplayName("Страница логина доступна без аутентификации")
    @Test
    void loginPageIsAccessibleWithoutAuthentication() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("Аутентифицированный USER получает доступ на чтение")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"/, главная страница", "/add, страница добавления книги", "/book/1, страница книги"})
    void authenticatedUserCanAccessProtectedPage(String url, String description) throws Exception {
        mvc.perform(get(url).with(user("testuser").roles("USER")))
                .andExpect(status().isOk());
    }

    @DisplayName("USER не может добавлять, редактировать и удалять книги (403)")
    @ParameterizedTest(name = "{0}")
    @MethodSource("userForbiddenPostRequests")
    void userCannotModifyBooks(String description, MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(request.with(user("testuser").roles("USER")))
                .andExpect(status().isForbidden());
    }

    static Stream<Arguments> userForbiddenPostRequests() {
        return Stream.of(
                Arguments.arguments("добавление книги", post("/add").with(csrf()).param("title", "New").param("authorId", "1").param("genreId", "1")),
                Arguments.arguments("удаление книги", post("/delete/1").with(csrf())),
                Arguments.arguments("обновление книги", post("/update/1").with(csrf()).param("title", "Updated").param("authorId", "1").param("genreId", "1"))
        );
    }

    @DisplayName("ADMIN может добавлять, редактировать и удалять книги")
    @ParameterizedTest(name = "{0}")
    @MethodSource("adminAllowedPostRequests")
    void adminCanModifyBooks(String description, MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(request.with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection());
    }

    static Stream<Arguments> adminAllowedPostRequests() {
        return Stream.of(
                Arguments.arguments("добавление книги", post("/add").with(csrf()).param("title", "New").param("authorId", "1").param("genreId", "1")),
                Arguments.arguments("удаление книги", post("/delete/1").with(csrf())),
                Arguments.arguments("обновление книги", post("/update/1").with(csrf()).param("title", "Updated").param("authorId", "1").param("genreId", "1"))
        );
    }

    @DisplayName("USER может добавлять комментарии")
    @Test
    void userCanAddComment() throws Exception {
        mvc.perform(post("/book/1/comment")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .param("text", "Мой комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @DisplayName("ADMIN может добавлять комментарии")
    @Test
    void adminCanAddComment() throws Exception {
        mvc.perform(post("/book/1/comment")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .param("text", "Комментарий администратора"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @DisplayName("GET /book/{id} возвращает страницу книги с моделью book и comments")
    @Test
    void bookDetailPage_returnsCorrectModelAndView() throws Exception {
        var bookDto = new ru.vavtech.hw13.models.dto.BookDto(1L, "Test Book",
                new ru.vavtech.hw13.models.dto.AuthorDto(1L, "Author"),
                new ru.vavtech.hw13.models.dto.GenreDto(1L, "Genre"));
        var comments = List.of(new ru.vavtech.hw13.models.dto.CommentDto(1L, "Комментарий", 1L, "user"));
        given(bookService.findById(1L)).willReturn(bookDto);
        given(commentService.findByBookId(1L)).willReturn(comments);

        mvc.perform(get("/book/1").with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("book-detail"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("comments", comments));
    }
}
