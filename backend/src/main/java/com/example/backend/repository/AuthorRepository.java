package com.example.backend.repository;

import com.example.backend.model.Author;
import com.example.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    @Query("SELECT b FROM Author a JOIN a.books b WHERE a.authorId = :authorId")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    List<Author> findByDeletedFalse();

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.authorId = :authorId AND b.deleted = false")
    List<Book> findActiveBooksByAuthorId(@Param("authorId") Long authorId);
}
