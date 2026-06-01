package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record AuthorDTO(
        Long authorId,

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @Size(max = 50, message = "Nationality must be less than 50 characters")
        String nationality,

        String portraitUrl,

        Set<BookDTO.BookSummaryDTO> books
) {
    public AuthorDTO withBooks(Set<BookDTO.BookSummaryDTO> books) {
        return new AuthorDTO(authorId, name, nationality, portraitUrl, books);
    }

    public record AuthorSummaryDTO(
            Long id,
            String name,
            String nationality,
            String portraitUrl
    ) {}
}
