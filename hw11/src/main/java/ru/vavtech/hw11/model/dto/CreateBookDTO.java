package ru.vavtech.hw11.model.dto;

import lombok.Data;

@Data
public class CreateBookDTO {

    private String title;

    private String authorId;

    private String genreId;
}
