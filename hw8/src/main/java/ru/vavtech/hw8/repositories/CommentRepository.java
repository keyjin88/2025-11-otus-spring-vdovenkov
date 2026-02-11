package ru.vavtech.hw8.repositories;


import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vavtech.hw8.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    @NonNull
    List<Comment> findByBookId(@NonNull String bookId);

    void deleteByBookId(@NonNull String bookId);

}
