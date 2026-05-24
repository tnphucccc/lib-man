package com.example.backend.service.borrowers;

import com.example.backend.dto.BorrowerDTO;
import com.example.backend.dto.BorrowerPatchDTO;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.LibraryMapper;
import com.example.backend.model.Borrower;
import com.example.backend.model.Borrowing;
import com.example.backend.repository.BorrowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowerService implements IBorrowerService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowerService.class);

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private LibraryMapper libraryMapper;

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
        BorrowerDTO borrowerDTO = libraryMapper.toBorrowerDTO(borrower);

        List<Borrowing> borrowings = borrowerRepository.findBorrowingByBorrowerId(borrower.getBorrowerId());
        borrowerDTO.setBorrowings(borrowings.stream()
                .map(libraryMapper::toBorrowingDTO)
                .collect(Collectors.toSet()));

        return borrowerDTO;
    }

    @Override
    @Transactional
    public BorrowerDTO createBorrower(BorrowerDTO.BorrowerSummaryDTO borrowerDTO) {
        logger.info("Creating a new borrower");
        Borrower borrower = new Borrower();
        borrower.setPhone(borrowerDTO.getPhone());
        borrower.setName(borrowerDTO.getName());
        borrower.setAddress(borrowerDTO.getAddress());
        borrower.setEmail(borrowerDTO.getEmail());

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

        if (patch.getName() != null) {
            existingBorrower.setName(patch.getName());
        }
        if (patch.getEmail() != null) {
            existingBorrower.setEmail(patch.getEmail());
        }
        if (patch.getPhone() != null) {
            existingBorrower.setPhone(patch.getPhone());
        }
        if (patch.getAddress() != null) {
            existingBorrower.setAddress(patch.getAddress());
        }
        if (patch.getStatus() != null) {
            existingBorrower.setStatus(Borrower.BorrowerStatus.valueOf(patch.getStatus()));
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
