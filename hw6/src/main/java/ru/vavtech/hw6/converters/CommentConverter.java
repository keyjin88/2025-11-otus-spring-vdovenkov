package ru.vavtech.hw6.converters;

import org.springframework.stereotype.Component;
import ru.vavtech.hw6.models.Comment;
import ru.vavtech.hw6.models.dto.CommentDto;


@Component
public class CommentConverter {

    public String commentToString(CommentDto commentDto) {
        return "Id: %d, text: %s".formatted(commentDto.getId(), commentDto.getComment());
    }

    public CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId());
    }
}
