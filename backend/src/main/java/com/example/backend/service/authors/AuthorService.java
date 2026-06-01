package com.example.backend.service.authors;

import com.example.backend.dto.AuthorDTO;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.LibraryMapper;
import com.example.backend.model.Author;
import com.example.backend.model.Book;
import com.example.backend.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final LibraryMapper libraryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        logger.info("Fetching all authors from the database");
        return authorRepository.findByDeletedFalse().stream()
                .map(author -> {
                    AuthorDTO authorDTO = libraryMapper.toAuthorDTO(author);
                    List<Book> books = authorRepository.findActiveBooksByAuthorId(author.getAuthorId());
                    authorDTO.setBooks(books.stream()
                            .map(libraryMapper::toBookSummaryDTO)
                            .collect(Collectors.toSet()));
                    return authorDTO;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long authorId) {
        logger.info("Fetching author with id: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        AuthorDTO authorDTO = libraryMapper.toAuthorDTO(author);
        List<Book> books = authorRepository.findBooksByAuthorId(author.getAuthorId());
        authorDTO.setBooks(books.stream()
                .map(libraryMapper::toBookSummaryDTO)
                .collect(Collectors.toSet()));
        return authorDTO;
    }

    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        logger.info("Creating a new author");
        Author author = libraryMapper.toAuthorEntity(authorDTO);
        Author newAuthor = authorRepository.save(author);
        logger.info("New author created with id: {}", newAuthor.getAuthorId());
        return libraryMapper.toAuthorDTO(newAuthor);
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long authorId, AuthorDTO authorDTO) {
        logger.info("Updating author with id: {}", authorId);
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        if (existingAuthor.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted author");
        }

        if (authorDTO.getName() != null) {
            existingAuthor.setName(authorDTO.getName());
        }
        if (authorDTO.getNationality() != null) {
            existingAuthor.setNationality(authorDTO.getNationality());
        }
        if (authorDTO.getPortraitUrl() != null) {
            existingAuthor.setPortraitUrl(authorDTO.getPortraitUrl());
        }

        Author updatedAuthor = authorRepository.save(existingAuthor);
        logger.info("Author updated successfully with id: {}", updatedAuthor.getAuthorId());
        return libraryMapper.toAuthorDTO(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long authorId) {
        logger.info("Soft-deleting author with id: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        author.setDeleted(true);
        authorRepository.save(author);
        logger.info("Author soft-deleted successfully with id: {}", authorId);
    }
}