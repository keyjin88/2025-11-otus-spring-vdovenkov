package ru.vavtech.hw6.services;


import ru.vavtech.hw6.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findByBookId(long bookId);

    CommentDto updateComment(long commentId, String commentText);

    CommentDto addComment(long bookId, String commentText);

    void deleteCommentById(long commentId);
}
