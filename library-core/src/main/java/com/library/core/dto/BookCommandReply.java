package com.library.core.dto;

public record BookCommandReply(
        String correlationId,
        boolean success,
        Long bookId,
        String message
) {
}