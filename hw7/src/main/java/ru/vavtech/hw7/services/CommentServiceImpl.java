package ru.vavtech.hw7.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw7.converters.CommentConverter;
import ru.vavtech.hw7.exceptions.EntityNotFoundException;
import ru.vavtech.hw7.models.Comment;
import ru.vavtech.hw7.models.dto.CommentDto;
import ru.vavtech.hw7.repositories.BookRepository;
import ru.vavtech.hw7.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentConverter::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateComment(long commentId, String commentText) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(commentId)));
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Transactional
    @Override
    public CommentDto addComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        Comment comment = new Comment(book);
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentConverter.from(comment);
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
