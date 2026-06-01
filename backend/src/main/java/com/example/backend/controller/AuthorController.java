package com.example.backend.controller;

import com.example.backend.dto.AuthorDTO;
import com.example.backend.service.authors.IAuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final IAuthorService authorService;

    @GetMapping({"", "/"})
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO newAuthor = authorService.createAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAuthor);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}