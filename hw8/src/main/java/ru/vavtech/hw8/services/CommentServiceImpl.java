package ru.vavtech.hw8.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw8.converters.CommentConverter;
import ru.vavtech.hw8.exceptions.EntityNotFoundException;
import ru.vavtech.hw8.models.Comment;
import ru.vavtech.hw8.models.dto.CommentDto;
import ru.vavtech.hw8.repositories.BookRepository;
import ru.vavtech.hw8.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @Override
    public List<CommentDto> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentConverter::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(String commentId, String commentText) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(commentId)));
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Override
    public CommentDto addComment(String bookId, String commentText) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Comment comment = new Comment(book);
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Override
    public void deleteCommentById(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
