package com.library.core.service;

import com.library.core.entity.BookingEntity;
import com.library.core.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private final static String ROOM_A = "A";
    private final static String ROOM_B = "B";
    private final BookingRepository bookingRepository;

    public List<BookingEntity> findAll() {
        List<BookingEntity> bookingEntityList = bookingRepository.findAll();
        return bookingEntityList;
    }

    public void book(BookingEntity bookingEntity) {
        String room = getRoom(bookingEntity);
        // set room
        bookingEntity.setRoom(room);
        bookingRepository.save(bookingEntity);
    }

    private String getRoom(BookingEntity bookingEntity) {

        // find existing room
        List<BookingEntity> bookingEntityList = bookingRepository.findAll();

        LocalDateTime regDate = bookingEntity.getRegisterTime();
        LocalDateTime toDateTime = regDate.plusMinutes(bookingEntity.getDuration());

        int hourFrom = regDate.getHour();
        int hourTo = toDateTime.getHour();
        int peoples = bookingEntity.getNumber();
        // validation A
        // default to A first
        if (peoples <= 5) {
            if (hourFrom >= 7 && hourTo <= 9) {
                return "A";
            } else if (hourFrom >= 7 && hourTo <= 9) {
                return "A";
            }
        }

        // validation B
        if (peoples > 5) {
            if (hourFrom >= 8 && hourTo <= 10) {
                return "B";
            } else if (hourFrom >= 10 && hourTo >= 11) {
                return "B";
            }
        }

        throw new RuntimeException("Room unavailable");
    }
}
