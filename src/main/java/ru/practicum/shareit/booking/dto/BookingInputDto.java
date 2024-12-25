package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingInputDto {
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    @Positive
    private Long itemId;
    private Long bookerId;
    private Status status;

    @AssertTrue
    public boolean isStartBeforeEnd() {
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }

    @Override
    public String toString() {
        return "BookingInputDto{" +
                "start=" + start +
                ", end=" + end +
                ", item=" + itemId +
                ", booker=" + bookerId +
                ", status=" + status +
                '}';
    }
}
