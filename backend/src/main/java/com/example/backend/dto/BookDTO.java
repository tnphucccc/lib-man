package com.example.backend.dto;

import com.example.backend.model.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BookDTO(
        Long bookId,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotBlank(message = "ISBN is required")
        @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
        String isbn,

        @NotNull(message = "Publication year is required")
        @Min(value = 1000, message = "Publication year must be at least 1000")
        Integer publicationYear,

        Set<AuthorDTO.AuthorSummaryDTO> authors,

        String status,

        String coverImageUrl
) {
    public BookDTO withAuthors(Set<AuthorDTO.AuthorSummaryDTO> authors) {
        return new BookDTO(bookId, title, isbn, publicationYear, authors, status, coverImageUrl);
    }

    public record BookSummaryDTO(
            Long id,
            String title,
            String isbn,
            Book.BookStatus status
    ) {}
}
