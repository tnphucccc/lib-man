package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingDTO {
    private Long borrowingId;

    private Long bookId;

    private Long borrowerId;

    private LocalDate borrowedDate = LocalDate.now();

    private LocalDate dueDate;

    private LocalDate returnedDate;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private String status;
}
