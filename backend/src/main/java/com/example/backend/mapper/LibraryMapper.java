package com.example.backend.mapper;

import com.example.backend.dto.AuthorDTO;
import com.example.backend.dto.BookDTO;
import com.example.backend.dto.BorrowerDTO;
import com.example.backend.dto.BorrowingDTO;
import com.example.backend.model.Author;
import com.example.backend.model.Book;
import com.example.backend.model.Borrower;
import com.example.backend.model.Borrowing;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {
    public BookDTO toBookDTO(Book book) {
        Set<AuthorDTO.AuthorSummaryDTO> authors = book.getAuthors() == null ? null :
                book.getAuthors().stream()
                        .map(this::toAuthorSummaryDTO)
                        .collect(Collectors.toSet());
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                authors,
                book.getStatus().name(),
                book.getCoverImageUrl()
        );
    }

    public BookDTO.BookSummaryDTO toBookSummaryDTO(Book book) {
        return new BookDTO.BookSummaryDTO(
                book.getBookId(),
                book.getTitle(),
                book.getIsbn(),
                book.getStatus()
        );
    }

    public AuthorDTO toAuthorDTO(Author author) {
        Set<BookDTO.BookSummaryDTO> books = author.getBooks() == null ? null :
                author.getBooks().stream()
                        .map(this::toBookSummaryDTO)
                        .collect(Collectors.toSet());
        return new AuthorDTO(
                author.getAuthorId(),
                author.getName(),
                author.getNationality(),
                author.getPortraitUrl(),
                books
        );
    }

    public AuthorDTO.AuthorSummaryDTO toAuthorSummaryDTO(Author author) {
        return new AuthorDTO.AuthorSummaryDTO(
                author.getAuthorId(),
                author.getName(),
                author.getNationality(),
                author.getPortraitUrl()
        );
    }

    public Author toAuthorEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.name());
        author.setNationality(authorDTO.nationality());
        author.setPortraitUrl(authorDTO.portraitUrl());
        return author;
    }

    public BorrowingDTO toBorrowingDTO(Borrowing borrowing) {
        return new BorrowingDTO(
                borrowing.getBorrowingId(),
                borrowing.getBook().getBookId(),
                borrowing.getBorrower().getBorrowerId(),
                borrowing.getBorrowedDate(),
                borrowing.getDueDate(),
                borrowing.getReturnedDate(),
                borrowing.getCreatedAt(),
                borrowing.getUpdatedAt(),
                borrowing.getStatus().name()
        );
    }

    public BorrowerDTO toBorrowerDTO(Borrower borrower) {
        return new BorrowerDTO(
                borrower.getBorrowerId(),
                borrower.getName(),
                borrower.getEmail(),
                borrower.getPhone(),
                borrower.getAddress(),
                borrower.getStatus().name(),
                null,
                borrower.getCreatedAt(),
                borrower.getUpdatedAt()
        );
    }

    public Borrower toBorrowerEntity(BorrowerDTO borrowerDTO) {
        Borrower borrower = new Borrower();
        borrower.setName(borrowerDTO.name());
        borrower.setEmail(borrowerDTO.email());
        borrower.setPhone(borrowerDTO.phone());
        borrower.setAddress(borrowerDTO.address());
        return borrower;
    }

    public BorrowerDTO.BorrowerSummaryDTO toBorrowerSummaryDTO(Borrower borrower) {
        return new BorrowerDTO.BorrowerSummaryDTO(
                borrower.getBorrowerId(),
                borrower.getName(),
                borrower.getEmail(),
                borrower.getPhone(),
                borrower.getAddress(),
                borrower.getStatus().name()
        );
    }
}
