package ru.vavtech.hw10.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.vavtech.hw10.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}
