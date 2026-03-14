package ru.vavtech.hw13.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw13.exceptions.NotFoundException;
import ru.vavtech.hw13.mapper.CommentMapper;
import ru.vavtech.hw13.models.Comment;
import ru.vavtech.hw13.models.User;
import ru.vavtech.hw13.models.dto.CommentDto;
import ru.vavtech.hw13.repositories.BookRepository;
import ru.vavtech.hw13.repositories.CommentRepository;
import ru.vavtech.hw13.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateComment(long commentId, String commentText) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(("Comment " +
                "with id %d not found").formatted(commentId)));
        checkCommentAccess(comment);
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto addComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException(("Book with id %d " +
                "not found").formatted(bookId)));
        User currentUser = getCurrentUser();
        Comment comment = new Comment(book);
        comment.setText(commentText);
        comment.setUser(currentUser);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(("Comment " +
                "with id %d not found").formatted(commentId)));
        checkCommentAccess(comment);
        commentRepository.deleteById(commentId);
    }

    private void checkCommentAccess(Comment comment) {
        if (isAdmin()) {
            return;
        }
        User owner = comment.getUser();
        if (owner == null) {
            throw new AccessDeniedException("Комментарий без владельца может изменять только администратор");
        }
        String currentUsername = getCurrentUsername();
        if (!owner.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Редактировать и удалять может только владелец комментария");
        }
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority(ROLE_ADMIN));
    }

    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + username));
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Требуется аутентификация");
        }
        return auth.getName();
    }
}
