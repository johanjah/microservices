package com.library.orchestrator.controller;

import com.library.orchestrator.dto.BookingDto;
import com.library.orchestrator.entity.BookingEntity;
import com.library.orchestrator.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAll() {
        List<BookingEntity> bookingEntityList = bookingService.findAll();
        return bookingEntityList.stream().map(this::toDto).toList();
    }

    @PostMapping
    public void book(@RequestBody BookingDto bookingDto) {

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setNumber(bookingDto.number());
        bookingEntity.setName(bookingDto.name());
        bookingEntity.setDuration(bookingDto.duration());
        bookingEntity.setRegisterTime(bookingDto.registerDate());

        bookingService.book(bookingEntity);
    }

    private BookingDto toDto(BookingEntity bookingEntity) {
        return new BookingDto(bookingEntity.getNumber(), bookingEntity.getDuration(), bookingEntity.getRegisterTime(), bookingEntity.getName());
    }

}
