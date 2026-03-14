package ru.vavtech.hw13.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vavtech.hw13.models.dto.UpdateBookDto;
import ru.vavtech.hw13.services.AuthorService;
import ru.vavtech.hw13.services.BookService;
import ru.vavtech.hw13.services.CommentService;
import ru.vavtech.hw13.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/")
    public String listBooksPage(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books-list";
    }

    @GetMapping("/add")
    public String addBookPage(Model model) {
        model.addAttribute("book", new UpdateBookDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "add-book";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") UpdateBookDto book,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "add-book";
        }
        bookService.create(book.getTitle(), book.getAuthorId(), book.getGenreId());
        return "redirect:/";
    }

    @GetMapping("/book/{id}")
    public String bookDetailPage(@PathVariable long id, Model model) {
        var bookDto = bookService.findById(id);
        model.addAttribute("book", bookDto);
        model.addAttribute("comments", commentService.findByBookId(id));
        return "book-detail";
    }

    @GetMapping("/edit/{id}")
    public String editBookPage(@PathVariable long id, Model model) {
        var bookDto = bookService.findById(id);
        model.addAttribute("book", UpdateBookDto.fromBookDto(bookDto));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "edit-book";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable long id,
                             @Valid @ModelAttribute("book") UpdateBookDto book,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "edit-book";
        }
        bookService.update(id, book.getTitle(), book.getAuthorId(), book.getGenreId());
        return "redirect:/";
    }
} 