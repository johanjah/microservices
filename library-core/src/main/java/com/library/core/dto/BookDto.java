package com.library.core.dto;

public record BookDto(
        Long id,
        String title,
        String author,
        String isbn
) {
}
