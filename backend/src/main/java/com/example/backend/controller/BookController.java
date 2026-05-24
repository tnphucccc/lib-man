package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.service.books.IBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping({"", "/"})
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO newBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO book) {
        BookDTO updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
