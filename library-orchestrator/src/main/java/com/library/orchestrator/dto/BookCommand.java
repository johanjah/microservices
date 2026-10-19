package com.library.orchestrator.dto;

public record BookCommand(
        String correlationId,
        String command,
        String title,
        String author,
        String isbn
) {
}