package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.ItemStatus;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody BookingInputDto bookingInputDto) {
        log.info("Поступил запрос пользователь с id = {} вещи на букинг. Body = {}", userId, bookingInputDto);
        return bookingClient.create(bookingInputDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        log.info("Поступил запрос пользователь с id = {} на изменение статуса вещи с id = {} на букинг {}",
                userId, bookingId, approved);
        return bookingClient.updateBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long bookingId) {
        log.info("Поступил запрос от пользователя с id = {} на получение Booking с id = {}", userId, bookingId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL") ItemStatus status) {
        log.info("Поступил запрос от пользователя с id = {} на получение букингов со статусом = {}", userId, status);
        return bookingClient.getAllUsersBookings(userId, status);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") ItemStatus status) {
        log.info("Поступил запрос пользователя с id = {} на получение своих букингов со статусом = {}", userId, status);
        return bookingClient.getAllOwnersBookings(userId, status);
    }
}