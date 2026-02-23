package ru.vavtech.hw8.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import ru.vavtech.hw8.models.Comment;
import ru.vavtech.hw8.repositories.BookRepository;
import ru.vavtech.hw8.repositories.CommentRepository;

import java.util.List;

@ChangeUnit(id = "insertComments", order = "004", author = "nrr")
public class InsertCommentsChangeUnit {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public InsertCommentsChangeUnit(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Execution
    public void execute() {
        Comment comment1 = new Comment("1", "Comment_1", bookRepository.findById("1").orElseThrow());
        Comment comment2 = new Comment("2", "Comment_2", bookRepository.findById("2").orElseThrow());
        Comment comment3 = new Comment("3", "Comment_3", bookRepository.findById("3").orElseThrow());
        Comment comment4 = new Comment("4", "Comment_4", bookRepository.findById("4").orElseThrow());
        Comment comment5 = new Comment("5", "Comment_5", bookRepository.findById("1").orElseThrow());
        Comment comment6 = new Comment("6", "Comment_6", bookRepository.findById("2").orElseThrow());
        Comment comment7 = new Comment("7", "Comment_7", bookRepository.findById("3").orElseThrow());

        commentRepository.insert(List.of(comment1, comment2, comment3, comment4, comment5, comment6, comment7));
    }

    @RollbackExecution
    public void rollback() {
        commentRepository.deleteAll();
    }
}
