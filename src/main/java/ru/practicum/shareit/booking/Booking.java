package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Instant;

@Data
public class Booking {
    @Positive
    private Long id;
    @NotNull
    private Instant start;
    @NotNull
    private Instant end;
    @Positive
    private Long item;
    @Positive
    private Long booker;
    @NotNull
    private Status status;
}