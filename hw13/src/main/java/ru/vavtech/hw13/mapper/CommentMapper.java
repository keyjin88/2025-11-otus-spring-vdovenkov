package ru.vavtech.hw13.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw13.models.Comment;
import ru.vavtech.hw13.models.dto.CommentDto;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        var ownerUsername = comment.getUser() != null ? comment.getUser().getUsername() : null;
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId(),
                ownerUsername);
    }
} 