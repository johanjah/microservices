package com.library.core.controller;

import com.library.core.dto.BookDashboardDto;
import com.library.core.service.BookDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class BookDashboardController {

    private final BookDashboardService bookDashboardService;

    @GetMapping("/books")
    public BookDashboardDto getBookDashboard() {
        return bookDashboardService.getBookDashboard();
    }
}
