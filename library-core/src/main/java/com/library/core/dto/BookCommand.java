package com.library.core.dto;

public record BookCommand(
        String correlationId,
        String command,
        String title,
        String author,
        String isbn
) {
}