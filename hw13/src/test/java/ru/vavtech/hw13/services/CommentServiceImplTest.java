package ru.vavtech.hw13.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vavtech.hw13.exceptions.NotFoundException;
import ru.vavtech.hw13.mapper.CommentMapper;
import ru.vavtech.hw13.models.Author;
import ru.vavtech.hw13.models.Book;
import ru.vavtech.hw13.models.Comment;
import ru.vavtech.hw13.models.Genre;
import ru.vavtech.hw13.models.User;
import ru.vavtech.hw13.models.dto.CommentDto;
import ru.vavtech.hw13.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@DisplayName("Тесты ACL CommentServiceImpl")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private long commentId;
    private long bookId;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Book book;
    private User owner;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentId = ThreadLocalRandom.current().nextLong(1, 100_000);
        bookId = ThreadLocalRandom.current().nextLong(1, 100_000);
        var author = new Author(1L, "Author");
        var genre = new Genre(1L, "Genre");
        book = new Book(bookId, "Test Book", author, genre);
        owner = new User(1L, "owner", "pass", "ROLE_USER");
        comment = new Comment(book);
        comment.setId(commentId);
        comment.setText("Текст комментария");
        comment.setUser(owner);

        lenient().when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("deleteCommentById выбрасывает AccessDeniedException, если пользователь не владелец и не ADMIN")
    @Test
    void deleteCommentById_throwsAccessDenied_whenUserIsNotOwnerAndNotAdmin() {
        setSecurityContext("otheruser", "ROLE_USER");

        assertThatThrownBy(() -> commentService.deleteCommentById(commentId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("владелец комментария");
    }

    @DisplayName("updateComment выбрасывает AccessDeniedException, если пользователь не владелец и не ADMIN")
    @Test
    void updateComment_throwsAccessDenied_whenUserIsNotOwnerAndNotAdmin() {
        setSecurityContext("otheruser", "ROLE_USER");

        assertThatThrownBy(() -> commentService.updateComment(commentId, "Новый текст"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("владелец комментария");
    }

    @DisplayName("deleteCommentById успешен, когда пользователь — владелец")
    @Test
    void deleteCommentById_succeeds_whenUserIsOwner() {
        setSecurityContext("owner", "ROLE_USER");

        assertThatCode(() -> commentService.deleteCommentById(commentId))
                .doesNotThrowAnyException();

        verify(commentRepository).deleteById(commentId);
    }

    @DisplayName("updateComment успешен, когда пользователь — владелец")
    @Test
    void updateComment_succeeds_whenUserIsOwner() {
        setSecurityContext("owner", "ROLE_USER");
        var updatedComment = new Comment(book);
        updatedComment.setId(commentId);
        updatedComment.setText("Обновлённый текст");
        updatedComment.setUser(owner);
        var commentDto = new CommentDto(commentId, "Обновлённый текст", bookId, "owner");

        given(commentRepository.save(any(Comment.class))).willReturn(updatedComment);
        given(commentMapper.toDto(any(Comment.class))).willReturn(commentDto);

        commentService.updateComment(commentId, "Обновлённый текст");

        verify(commentRepository).save(any(Comment.class));
    }

    @DisplayName("deleteCommentById успешен, когда пользователь — ADMIN")
    @Test
    void deleteCommentById_succeeds_whenUserIsAdmin() {
        setSecurityContext("admin", "ROLE_ADMIN");

        assertThatCode(() -> commentService.deleteCommentById(commentId))
                .doesNotThrowAnyException();

        verify(commentRepository).deleteById(commentId);
    }

    @DisplayName("updateComment успешен, когда пользователь — ADMIN")
    @Test
    void updateComment_succeeds_whenUserIsAdmin() {
        setSecurityContext("admin", "ROLE_ADMIN");
        var updatedComment = new Comment(book);
        updatedComment.setId(commentId);
        updatedComment.setText("Обновлено админом");
        updatedComment.setUser(owner);
        var commentDto = new CommentDto(commentId, "Обновлено админом", bookId, "owner");

        given(commentRepository.save(any(Comment.class))).willReturn(updatedComment);
        given(commentMapper.toDto(any(Comment.class))).willReturn(commentDto);

        commentService.updateComment(commentId, "Обновлено админом");

        verify(commentRepository).save(any(Comment.class));
    }

    @DisplayName("deleteCommentById выбрасывает NotFoundException, если комментарий не найден")
    @Test
    void deleteCommentById_throwsNotFound_whenCommentDoesNotExist() {
        setSecurityContext("owner", "ROLE_USER");
        long notFoundId = commentId + 100_000;
        given(commentRepository.findById(notFoundId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteCommentById(notFoundId))
                .isInstanceOf(NotFoundException.class);
    }

    private void setSecurityContext(String username, String role) {
        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
