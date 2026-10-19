package com.library.core.service;

import com.library.core.dto.BookDashboardDto;
import com.library.core.dto.BookDashboardProjection;
import com.library.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookDashboardService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookDashboardDto getBookDashboard() {

        BookDashboardProjection result =
                bookRepository.getBookDashboard();

        return new BookDashboardDto(
                result.getTotalBooks(),
                result.getTotalAuthors(),
                result.getBooksAddedLast30Days(),
                result.getLatestBookCreatedAt()
        );
    }
}
