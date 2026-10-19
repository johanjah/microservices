package com.library.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room")
    private String room;

    @Column(name = "number")
    private Integer number;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "reg_time")
    private LocalDateTime registerTime;

    @Column(name = "name")
    private String name;
}
