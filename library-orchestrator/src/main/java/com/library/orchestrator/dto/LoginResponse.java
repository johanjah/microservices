package com.library.orchestrator.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
