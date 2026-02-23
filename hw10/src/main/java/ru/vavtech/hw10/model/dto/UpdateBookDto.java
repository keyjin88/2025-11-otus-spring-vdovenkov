package ru.vavtech.hw10.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title length should be between 2 and 100 characters")
    private String title;

    @NotNull(message = "Author is required")
    private Long authorId;

    @NotNull(message = "Genre is required")
    private Long genreId;
} 