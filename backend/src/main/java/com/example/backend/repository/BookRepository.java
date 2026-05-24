package com.example.backend.repository;

import com.example.backend.model.Author;
import com.example.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT a FROM Book b JOIN b.authors a WHERE b.bookId = :bookId")
    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);

    List<Book> findByDeletedFalse();
}
