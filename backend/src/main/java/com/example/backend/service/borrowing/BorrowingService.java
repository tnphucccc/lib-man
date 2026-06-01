package com.example.backend.service.borrowing;

import com.example.backend.dto.BorrowingDTO;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.LibraryMapper;
import com.example.backend.model.Book;
import com.example.backend.model.Borrower;
import com.example.backend.model.Borrowing;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.BorrowerRepository;
import com.example.backend.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowingService implements IBorrowingService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingService.class);

    private final BorrowingRepository borrowingRepository;

    private final BookRepository bookRepository;

    private final BorrowerRepository borrowerRepository;

    private final LibraryMapper libraryMapper;

    @Override
    public List<BorrowingDTO> getAllBorrowings() {
        logger.info("Fetching all borrowings from the database");
        List<Borrowing> borrowings = borrowingRepository.findAll();
        borrowings.forEach(this::updateOverdueStatus);
        return borrowings.stream()
                .map(libraryMapper::toBorrowingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowingDTO getBorrowingById(Long borrowingId) {
        logger.info("Fetching borrowing with id: {}", borrowingId);
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + borrowingId));
        updateOverdueStatus(borrowing);
        return libraryMapper.toBorrowingDTO(borrowing);
    }

    @Override
    @Transactional
    public BorrowingDTO createBorrowing(BorrowingDTO borrowingDTO) {
        logger.info("Creating a new borrowing");

        Book book = bookRepository.findById(borrowingDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + borrowingDTO.getBookId()));

        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Book is not available for borrowing (current status: " + book.getStatus() + ")");
        }

        Borrower borrower = borrowerRepository.findById(borrowingDTO.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowingDTO.getBorrowerId()));

        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowedDate(LocalDate.now());
        borrowing.setDueDate(borrowingDTO.getDueDate());
        borrowing.setStatus(Borrowing.BorrowingStatus.BORROWED);
        borrowing.setBorrower(borrower);
        borrowing.setBook(book);

        Borrowing saveBorrowing = borrowingRepository.save(borrowing);

        book.setStatus(Book.BookStatus.BORROWED);
        bookRepository.save(book);

        logger.info("Borrowing created successfully with id: {}", saveBorrowing.getBorrowingId());
        return libraryMapper.toBorrowingDTO(saveBorrowing);
    }

    @Override
    @Transactional
    public BorrowingDTO updateBorrowing(Long borrowingId, BorrowingDTO borrowingDTO) {
        logger.info("Updating borrowing with id: {}", borrowingId);
        Borrowing existingBorrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + borrowingId));

        if (borrowingDTO.getBorrowerId() != null) {
            Borrower borrower = borrowerRepository.findById(borrowingDTO.getBorrowerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowingDTO.getBorrowerId()));
            existingBorrowing.setBorrower(borrower);
        }
        if (borrowingDTO.getBookId() != null) {
            Book book = bookRepository.findById(borrowingDTO.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + borrowingDTO.getBookId()));
            existingBorrowing.setBook(book);
        }
        if (borrowingDTO.getBorrowedDate() != null) {
            existingBorrowing.setBorrowedDate(borrowingDTO.getBorrowedDate());
        }
        if (borrowingDTO.getDueDate() != null) {
            existingBorrowing.setDueDate(borrowingDTO.getDueDate());
        }

        if (borrowingDTO.getReturnedDate() != null) {
            existingBorrowing.setReturnedDate(borrowingDTO.getReturnedDate());
        }

        if (borrowingDTO.getStatus() != null) {
            existingBorrowing.setStatus(Borrowing.BorrowingStatus.valueOf(borrowingDTO.getStatus()));
            if (existingBorrowing.getStatus().equals(Borrowing.BorrowingStatus.RETURNED)) {
                Book book = existingBorrowing.getBook();
                book.setStatus(Book.BookStatus.AVAILABLE);
                bookRepository.save(book);
            }
        }

        Borrowing updateBorrowing = borrowingRepository.save(existingBorrowing);
        logger.info("Borrowing updated successfully with id: {}", updateBorrowing.getBorrowingId());
        return libraryMapper.toBorrowingDTO(updateBorrowing);
    }

    @Override
    @Transactional
    public void deleteBorrowing(Long borrowingId) {
        logger.info("Deleting borrowing with id: {}", borrowingId);
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + borrowingId));
        borrowingRepository.delete(borrowing);
        logger.info("Borrowing deleted successfully with id: {}", borrowingId);
    }

    private void updateOverdueStatus(Borrowing borrowing) {
        if (borrowing.getDueDate().isBefore(LocalDate.now()) && borrowing.getStatus().equals(Borrowing.BorrowingStatus.BORROWED)) {
            borrowing.setStatus(Borrowing.BorrowingStatus.OVERDUE);
            borrowingRepository.save(borrowing);
        }
    }
}
