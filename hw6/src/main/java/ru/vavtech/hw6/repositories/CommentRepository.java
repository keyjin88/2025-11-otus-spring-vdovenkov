package ru.vavtech.hw6.repositories;


import ru.vavtech.hw6.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long commentId);

    List<Comment> findByBookId(long bookId);

    Comment save(Comment comment);

    void deleteById(long commentId);
}
