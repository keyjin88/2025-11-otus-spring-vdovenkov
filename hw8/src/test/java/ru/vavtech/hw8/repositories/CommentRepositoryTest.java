package ru.vavtech.hw8.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.vavtech.hw8.models.Book;
import ru.vavtech.hw8.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldFindCommentById() {
        var actualComment = commentRepository.findById("1");
        var expected = mongoTemplate.findById("1", Comment.class);
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getText()).isEqualTo(expected.getText());
        assertThat(actualComment.get().getBook().getId()).isEqualTo(expected.getBook().getId());
    }

    @Test
    void shouldReturnEmptyCommentListWhenBookAbsent() {
        var comments = commentRepository.findByBookId("not exist");
        assertThat(comments).isEmpty();
    }

    @Test
    void shouldReturnCommentsByBookId() {
        var comment1 = mongoTemplate.findById("1", Comment.class);
        var comment2 = mongoTemplate.findById("5", Comment.class);
        var comments = commentRepository.findByBookId("1");

        assertThat(comments.contains(comment1)).isTrue();
        assertThat(comments.contains(comment2)).isTrue();
    }

    @Test
    void shouldSaveNewComment() {
        var book = mongoTemplate.findById("2", Book.class);
        var comment = new Comment(book);
        comment.setText("new comment");

        var actual = commentRepository.save(comment);
        var expected = mongoTemplate.findById(actual.getId(), Comment.class);
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateComment() {
        var comment = mongoTemplate.findById("3", Comment.class);
        comment.setText(java.util.UUID.randomUUID().toString());

        var actual = commentRepository.save(comment);

        var expected = mongoTemplate.findById(comment.getId(), Comment.class);
        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(expected.getText());
        assertThat(actual.getBook().getId()).isEqualTo(expected.getBook().getId());
    }

    @Test
    void shoudDeleteCommentById() {
        var comment = mongoTemplate.findById("6", Comment.class);
        assertThat(comment).isNotNull();

        commentRepository.deleteById(comment.getId());

        assertThat(mongoTemplate.findById("6", Comment.class)).isNull();
    }
}