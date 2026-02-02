package ru.vavtech.hw7.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw7.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}
