package com.example.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BorrowingDTO(
        Long borrowingId,
        Long bookId,
        Long borrowerId,
        LocalDate borrowedDate,
        LocalDate dueDate,
        LocalDate returnedDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status
) {}
