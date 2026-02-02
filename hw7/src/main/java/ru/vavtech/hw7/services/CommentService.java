package ru.vavtech.hw7.services;


import ru.vavtech.hw7.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findByBookId(long bookId);

    CommentDto updateComment(long commentId, String commentText);

    CommentDto addComment(long bookId, String commentText);

    void deleteCommentById(long commentId);
}
