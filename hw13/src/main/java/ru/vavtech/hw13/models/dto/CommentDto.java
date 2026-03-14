package ru.vavtech.hw13.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;

    private String comment;

    private long bookId;

    private String ownerUsername;

    public CommentDto(long id, String comment, long bookId) {
        this.id = id;
        this.comment = comment;
        this.bookId = bookId;
        this.ownerUsername = null;
    }
}
