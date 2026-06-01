package com.example.backend.service.borrowers;

import com.example.backend.dto.BorrowerDTO;
import com.example.backend.dto.BorrowerPatchDTO;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.LibraryMapper;
import com.example.backend.model.Borrower;
import com.example.backend.model.Borrowing;
import com.example.backend.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowerService implements IBorrowerService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowerService.class);

    private final BorrowerRepository borrowerRepository;

    private final LibraryMapper libraryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BorrowerDTO.BorrowerSummaryDTO> getAllBorrowers() {
        logger.info("Fetching all borrowers from the database");
        return borrowerRepository.findByDeletedFalse().stream()
                .map(libraryMapper::toBorrowerSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowerDTO getBorrowerById(Long borrowerID) {
        logger.info("Fetching borrower with id: {}", borrowerID);

        Borrower borrower = borrowerRepository.findById(borrowerID)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerID));

        List<Borrowing> borrowings = borrowerRepository.findBorrowingByBorrowerId(borrower.getBorrowerId());
        return libraryMapper.toBorrowerDTO(borrower)
                .withBorrowings(borrowings.stream()
                        .map(libraryMapper::toBorrowingDTO)
                        .collect(Collectors.toSet()));
    }

    @Override
    @Transactional
    public BorrowerDTO createBorrower(BorrowerDTO.BorrowerSummaryDTO borrowerDTO) {
        logger.info("Creating a new borrower");
        Borrower borrower = new Borrower();
        borrower.setPhone(borrowerDTO.phone());
        borrower.setName(borrowerDTO.name());
        borrower.setAddress(borrowerDTO.address());
        borrower.setEmail(borrowerDTO.email());

        Borrower savedBorrower = borrowerRepository.save(borrower);
        logger.info("Borrower created successfully with id: {}", savedBorrower.getBorrowerId());
        return libraryMapper.toBorrowerDTO(savedBorrower);
    }

    @Override
    @Transactional
    public BorrowerDTO updateBorrower(Long borrowerID, BorrowerPatchDTO patch) {
        logger.info("Updating borrower with id: {}", borrowerID);
        Borrower existingBorrower = borrowerRepository.findById(borrowerID)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerID));
        if (existingBorrower.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted borrower");
        }

        if (patch.name() != null) {
            existingBorrower.setName(patch.name());
        }
        if (patch.email() != null) {
            existingBorrower.setEmail(patch.email());
        }
        if (patch.phone() != null) {
            existingBorrower.setPhone(patch.phone());
        }
        if (patch.address() != null) {
            existingBorrower.setAddress(patch.address());
        }
        if (patch.status() != null) {
            existingBorrower.setStatus(Borrower.BorrowerStatus.valueOf(patch.status()));
        }

        Borrower updatedBorrower = borrowerRepository.save(existingBorrower);
        logger.info("Borrower updated successfully with id: {}", updatedBorrower.getBorrowerId());
        return libraryMapper.toBorrowerDTO(updatedBorrower);
    }

    @Override
    @Transactional
    public void deleteBorrower(Long borrowerID) {
        logger.info("Soft-deleting borrower with id: {}", borrowerID);
        Borrower borrower = borrowerRepository.findById(borrowerID)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerID));
        borrower.setDeleted(true);
        borrowerRepository.save(borrower);
        logger.info("Borrower soft-deleted successfully with id: {}", borrowerID);
    }
}
