package com.library.core.dto;

import java.time.LocalDateTime;

public interface BookDashboardProjection {

    Long getTotalBooks();

    Long getTotalAuthors();

    Long getBooksAddedLast30Days();

    LocalDateTime getLatestBookCreatedAt();
}
