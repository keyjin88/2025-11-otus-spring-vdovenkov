package ru.vavtech.hw8.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.vavtech.hw8.models.Author;
import ru.vavtech.hw8.models.Book;
import ru.vavtech.hw8.models.Comment;
import ru.vavtech.hw8.models.Genre;

import java.util.List;

/**
 * Инициализатор тестовых данных для репозиторных тестов.
 * Используется когда Mongock отключён (mongock.enabled=false).
 */
@Component
@Profile("test")
public class MongoTestDataInitializer {

    private final MongoTemplate mongoTemplate;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public MongoTestDataInitializer(MongoTemplate mongoTemplate,
                                   MongoDatabaseFactory mongoDatabaseFactory,
                                   AuthorRepository authorRepository,
                                   GenreRepository genreRepository,
                                   BookRepository bookRepository,
                                   CommentRepository commentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @PostConstruct
    void initData() {
        mongoDatabaseFactory.getMongoDatabase().drop();

        var genre1 = new Genre("1", "Genre_1");
        var genre2 = new Genre("2", "Genre_2");
        var genre3 = new Genre("3", "Genre_3");
        genreRepository.insert(List.of(genre1, genre2, genre3));

        var author1 = new Author("1", "Author_1");
        var author2 = new Author("2", "Author_2");
        var author3 = new Author("3", "Author_3");
        authorRepository.insert(List.of(author1, author2, author3));

        var book1 = new Book("1", "Book_1", author2, List.of(genre2, genre1));
        var book2 = new Book("2", "Book_2", author1, List.of(genre3, genre2));
        var book3 = new Book("3", "Book_3", author1, List.of(genre1));
        var book4 = new Book("4", "Book_4", author3, List.of(genre3, genre1));
        bookRepository.insert(List.of(book1, book2, book3, book4));

        var comment1 = new Comment("1", "Comment_1", book1);
        var comment2 = new Comment("2", "Comment_2", book2);
        var comment3 = new Comment("3", "Comment_3", book3);
        var comment4 = new Comment("4", "Comment_4", book4);
        var comment5 = new Comment("5", "Comment_5", book1);
        var comment6 = new Comment("6", "Comment_6", book2);
        var comment7 = new Comment("7", "Comment_7", book3);
        commentRepository.insert(List.of(comment1, comment2, comment3, comment4, comment5, comment6, comment7));
    }
}
