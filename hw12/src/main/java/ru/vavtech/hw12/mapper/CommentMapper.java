package ru.vavtech.hw12.mapper;

import org.springframework.stereotype.Component;
import ru.vavtech.hw12.models.Comment;
import ru.vavtech.hw12.models.dto.CommentDto;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId());
    }
} 