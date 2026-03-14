package ru.vavtech.hw13.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vavtech.hw13.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/book/{bookId}/comment")
    public String addComment(@PathVariable long bookId, @RequestParam String text) {
        commentService.addComment(bookId, text);
        return "redirect:/book/" + bookId;
    }

    @PostMapping("/comment/{id}/update")
    public String updateComment(@PathVariable long id, @RequestParam String text) {
        var comment = commentService.updateComment(id, text);
        return "redirect:/book/" + comment.getBookId();
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable long id, @RequestParam long bookId) {
        commentService.deleteCommentById(id);
        return "redirect:/book/" + bookId;
    }
}
