package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

public record BorrowerDTO(
        Long borrowerId,

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Size(max = 50, message = "Phone number must be less than 50 characters")
        String phone,

        @Size(max = 255, message = "Address must be less than 255 characters")
        String address,

        @NotBlank(message = "Status is required")
        String status,

        Set<BorrowingDTO> borrowings,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
    public BorrowerDTO withBorrowings(Set<BorrowingDTO> borrowings) {
        return new BorrowerDTO(borrowerId, name, email, phone, address, status, borrowings, createdAt, updatedAt);
    }

    public record BorrowerSummaryDTO(
            Long id,
            String name,
            String email,
            String phone,
            String address,
            String status
    ) {}
}
