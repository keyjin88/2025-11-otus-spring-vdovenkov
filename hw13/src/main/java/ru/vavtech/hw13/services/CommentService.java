package ru.vavtech.hw13.services;


import ru.vavtech.hw13.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findByBookId(long bookId);

    CommentDto updateComment(long commentId, String commentText);

    CommentDto addComment(long bookId, String commentText);

    void deleteCommentById(long commentId);
}
