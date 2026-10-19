package com.library.core.service;

import com.library.core.dto.BookCommand;
import com.library.core.dto.BookDto;
import com.library.core.entity.BookEntity;
import com.library.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Long createBook(BookCommand command) {

        if (!"CREATE_BOOK".equals(command.command())) {
            throw new IllegalArgumentException(
                    "Command unsupported: " + command.command()
            );
        }

        if (command.title() == null || command.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (command.author() == null || command.author().isBlank()) {
            throw new IllegalArgumentException("Author is required");
        }

        if (command.isbn() == null || command.isbn().isBlank()) {
            throw new IllegalArgumentException("ISBN is required");
        }

        if (bookRepository.existsByIsbn(command.isbn())) {
            throw new IllegalArgumentException(
                    "ISBN already registered: " + command.isbn()
            );
        }

        BookEntity book = new BookEntity();
        book.setTitle(command.title());
        book.setAuthor(command.author());
        book.setIsbn(command.isbn());

        BookEntity savedBook = bookRepository.save(book);

        return savedBook.getId();
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @CacheEvict(value = "core_books", key = "#id")
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Cacheable(value = "core_books", key = "#id")
    public BookDto getBookById(Long id) {
        BookEntity b = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        return convertToDto(b);
    }

    public BookDto convertToDto(BookEntity book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn()
        );
    }
}