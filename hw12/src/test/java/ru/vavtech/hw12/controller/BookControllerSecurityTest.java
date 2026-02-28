package ru.vavtech.hw12.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты проверки защиты ресурсов Spring Security.
 */
@DisplayName("Тесты защиты ресурсов")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Неаутентифицированный GET перенаправляет на логин")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"/, главная страница", "/add, страница добавления книги", "/edit/1, страница редактирования книги"})
    void unauthenticatedGetRedirectsToLogin(String url, String description) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @DisplayName("Неаутентифицированный POST возвращает 403")
    @ParameterizedTest(name = "{0}")
    @MethodSource("unauthenticatedPostRequests")
    void unauthenticatedPostReturns403(String description, MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    static Stream<Arguments> unauthenticatedPostRequests() {
        return Stream.of(
                Arguments.arguments(
                        "добавление книги",
                        post("/add")
                                .param("title", "New Book")
                                .param("authorId", "1")
                                .param("genreId", "1")
                ),
                Arguments.arguments(
                        "удаление книги",
                        post("/delete/1")
                )
        );
    }

    @DisplayName("Страница логина доступна без аутентификации")
    @Test
    void loginPageIsAccessibleWithoutAuthentication() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("Аутентифицированный пользователь получает доступ")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"/, главная страница", "/add, страница добавления книги"})
    void authenticatedUserCanAccessProtectedPage(String url, String description) throws Exception {
        mvc.perform(get(url).with(user("testuser").roles("USER")))
                .andExpect(status().isOk());
    }
}
