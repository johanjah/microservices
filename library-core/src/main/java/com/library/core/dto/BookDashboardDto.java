package com.library.core.dto;

import java.time.LocalDateTime;

public record BookDashboardDto(
        Long totalBooks,
        Long totalAuthors,
        Long booksAddedLast30Days,
        LocalDateTime latestBookCreatedAt
) {
}
