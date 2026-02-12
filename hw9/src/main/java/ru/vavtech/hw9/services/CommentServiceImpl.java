package ru.vavtech.hw9.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vavtech.hw9.exceptions.NotFoundException;
import ru.vavtech.hw9.mapper.CommentMapper;
import ru.vavtech.hw9.models.Comment;
import ru.vavtech.hw9.models.dto.CommentDto;
import ru.vavtech.hw9.repositories.BookRepository;
import ru.vavtech.hw9.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateComment(long commentId, String commentText) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(("Comment " +
                "with id %d not found").formatted(commentId)));
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto addComment(long bookId, String commentText) {
        var book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException(("Book with id %d " +
                "not found").formatted(bookId)));
        Comment comment = new Comment(book);
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
