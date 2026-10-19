package com.library.orchestrator.dto;

import java.time.LocalDateTime;

public record BookingDto(
        //String room,
        Integer number,
        Integer duration,
        LocalDateTime registerDate,
        String name
) {
}
