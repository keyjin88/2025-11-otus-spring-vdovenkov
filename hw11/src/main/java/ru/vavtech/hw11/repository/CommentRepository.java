package ru.vavtech.hw11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.vavtech.hw11.model.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
}
