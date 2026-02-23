package ru.vavtech.hw10.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw10.mapper.CommentMapper;
import ru.vavtech.hw10.model.Comment;
import ru.vavtech.hw10.model.dto.CommentDto;
import ru.vavtech.hw10.repository.BookRepository;
import ru.vavtech.hw10.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto addComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(0, commentText, book);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto updateComment(long commentId, String commentText) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id %d not found".formatted(commentId)));
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
