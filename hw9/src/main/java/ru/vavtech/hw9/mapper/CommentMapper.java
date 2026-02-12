package ru.vavtech.hw9.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw9.models.Comment;
import ru.vavtech.hw9.models.dto.CommentDto;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId());
    }
} 