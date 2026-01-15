package ru.vavtech.hw5.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.vavtech.hw5.exceptions.EntityNotFoundException;
import ru.vavtech.hw5.models.Author;
import ru.vavtech.hw5.models.Book;
import ru.vavtech.hw5.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcOps;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        var query = """
                select b.id, b.title, a.id as a_id, a.full_name, g.id as g_id, g.name 
                from books b 
                join authors a on b.author_id = a.id 
                join books_genres bg on b.id = bg.book_id  
                join genres g on bg.genre_id = g.id 
                where b.id = :id
                """;
        var book = jdbcOps.query(query, Map.of("id", id), new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcOps.update("delete from books where id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        var query = """
                select b.id, b.title, a.id as a_id, a.full_name from books b
                join authors a on b.author_id = a.id 
                """;
        return jdbcOps.query(query, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOps.query("select book_id, genre_id from books_genres", new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        Map<Long, Genre> genreById = genres.stream().collect(toMap(Genre::getId, identity()));
        Map<Long, List<Genre>> genresByBookId = relations.stream()
                .collect(groupingBy(BookGenreRelation::bookId, mapping(r -> genreById.get(r.genreId), toList())));
        booksWithoutGenres.forEach(book -> book.setGenres(genresByBookId.getOrDefault(book.getId(), List.of())));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...
        var params = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId()
        ));
        jdbcOps.update("insert into books (title, author_id) values (:title, :author_id)", params, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        //...
        var params = Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId()
        );
        var query = """
                update books 
                set title = :title, author_id = :author_id
                where id = :id
                """;
        int updated = jdbcOps.update(query, params);
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (0 == updated) {
            throw new EntityNotFoundException("Book with id %d not found!".formatted(book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
        var params = book.getGenres().stream()
                .map(genre -> Map.of("book_id", book.getId(), "genre_id", genre.getId()))
                .map(MapSqlParameterSource::new)
                .toList().toArray(new MapSqlParameterSource[0]);
        jdbcOps.batchUpdate("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)", params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcOps.update("delete from books_genres where book_id = :id", Map.of("id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var authorId = rs.getLong("a_id");
            var fullName = rs.getString("full_name");
            var author = new Author(authorId, fullName);
            var bookId = rs.getLong("id");
            var title = rs.getString("title");
            return new Book(bookId, title, author, List.of());
        }

    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong("book_id");
            var genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }

    }

    // Использовать для findById
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                var authorId = rs.getLong("a_id");
                var fullName = rs.getString("full_name");
                var author = new Author(authorId, fullName);
                var bookId = rs.getLong("id");
                var title = rs.getString("title");
                var book = new Book(bookId, title, author, List.of());
                List<Genre> genres = new ArrayList<>();
                do {
                    var genreId = rs.getLong("g_id");
                    var name = rs.getString("name");
                    genres.add(new Genre(genreId, name));
                } while (rs.next());
                book.setGenres(genres);
                return book;
            }
            return null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}