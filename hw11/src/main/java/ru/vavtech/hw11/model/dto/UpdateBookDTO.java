package ru.vavtech.hw11.model.dto;

import lombok.Data;

@Data
public class UpdateBookDTO {

    private String title;

    private String authorId;

    private String genreId;
}
