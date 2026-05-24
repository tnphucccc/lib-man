package com.example.backend.controller;

import com.example.backend.dto.BorrowerDTO;
import com.example.backend.dto.BorrowerPatchDTO;
import com.example.backend.service.borrowers.IBorrowerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowers")
public class BorrowerController {
    @Autowired
    private IBorrowerService borrowerService;

    @GetMapping({"", "/"})
    public List<BorrowerDTO.BorrowerSummaryDTO> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BorrowerDTO> getBorrowerById(@PathVariable Long id) {
        BorrowerDTO borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<BorrowerDTO> createBorrower(@Valid @RequestBody BorrowerDTO.BorrowerSummaryDTO borrowerDTO) {
        BorrowerDTO newBorrower = borrowerService.createBorrower(borrowerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBorrower);
    }

    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BorrowerDTO> updateBorrower(@PathVariable Long id, @Valid @RequestBody BorrowerPatchDTO borrowerPatchDTO) {
        BorrowerDTO updatedBorrower = borrowerService.updateBorrower(id, borrowerPatchDTO);
        return ResponseEntity.ok(updatedBorrower);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }
}