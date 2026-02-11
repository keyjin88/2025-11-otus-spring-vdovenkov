package ru.vavtech.hw8.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@EqualsAndHashCode(exclude = "book")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;

    private String text;

    @DBRef(lazy = true)
    private Book book;

    public Comment(Book book) {
        this.book = book;
    }
}
