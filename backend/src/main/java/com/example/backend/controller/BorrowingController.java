package com.example.backend.controller;

import com.example.backend.dto.BorrowingDTO;
import com.example.backend.service.borrowing.IBorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final IBorrowingService borrowingService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<BorrowingDTO>> getAllBorrowings() {
        List<BorrowingDTO> borrowings = borrowingService.getAllBorrowings();
        return new ResponseEntity<>(borrowings, HttpStatus.OK);
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BorrowingDTO> getBorrowingById(@PathVariable("id") Long id) {
        BorrowingDTO borrowing = borrowingService.getBorrowingById(id);
        return new ResponseEntity<>(borrowing, HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<BorrowingDTO> createBorrowing(@Valid @RequestBody BorrowingDTO borrowingDTO) {
        BorrowingDTO createdBorrowing = borrowingService.createBorrowing(borrowingDTO);
        return new ResponseEntity<>(createdBorrowing, HttpStatus.CREATED);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BorrowingDTO> updateBorrowing(
            @PathVariable("id") Long id,
            @Valid @RequestBody BorrowingDTO borrowingDTO) {
        BorrowingDTO updatedBorrowing = borrowingService.updateBorrowing(id, borrowingDTO);
        return new ResponseEntity<>(updatedBorrowing, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Void> deleteBorrowing(@PathVariable("id") Long id) {
        borrowingService.deleteBorrowing(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}