package ru.vavtech.hw8.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;
import ru.vavtech.hw8.converters.CommentConverter;
import ru.vavtech.hw8.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @Command(name = "bcs",
            description = "Показать комментарии к книге", group = "Комментарии")
    public String showComments(
            @Option(shortName = 'b', longName = "bookId", description = "Id книги", required = true) String bookId
    ) {
        var comments = commentService.findByBookId(bookId)
                .stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return "No comments available";
        }
        return String.join(System.lineSeparator(), comments);
    }

    @Command(name = "bcu", description = "Изменить комментарий", group = "Комментарии")
    public String updateComment(
            @Option(shortName = 'c', longName = "commentId",
                    description = "Id комментария", required = true) String commentId,
            @Option(shortName = 't', longName = "text", description = "Новый текст", required = true) String changedText
    ) {
        var comment = commentService.updateComment(commentId, changedText);
        return commentConverter.commentToString(comment);
    }

    @Command(name = "bcd", description = "Удалить комментарий", group = "Комментарии")
    public void deleteComment(@Option(shortName = 'c', longName = "commentId",
            description = "Id комментария", required = true) String commentId) {
        commentService.deleteCommentById(commentId);
    }

    @Command(name = "bcn", description = "Добавить комментарий к книге", group = "Комментарии")
    public String addCommentForBook(@Option(shortName = 'b', longName = "bookId",
                                            description = "Id книги", required = true) String bookId,
                                    @Option(shortName = 't', longName = "text",
                                            description = "Текст комментария", required = true) String commentText) {
        var comment = commentService.addComment(bookId, commentText);
        return commentConverter.commentToString(comment);
    }
}
