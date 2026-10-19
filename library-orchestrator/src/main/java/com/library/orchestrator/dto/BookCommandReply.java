package com.library.orchestrator.dto;

public record BookCommandReply(
        String correlationId,
        boolean success,
        Long bookId,
        String message
) {
}