package ru.vavtech.hw9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw9.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}
