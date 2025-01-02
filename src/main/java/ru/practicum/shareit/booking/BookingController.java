package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.ItemStatus;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody BookingInputDto bookingInputDto) {
        log.info("Поступил запрос пользователь с id = {} вещи на букинг. Body = {}", userId, bookingInputDto);
        return bookingService.create(bookingInputDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        log.info("Поступил запрос пользователь с id = {} на изменение статуса вещи с id = {} на букинг {}",
                userId, bookingId, approved);
        return bookingService.updateBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long bookingId) {
        log.info("Поступил запрос от пользователя с id = {} на получение Booking с id = {}", userId, bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutputDto> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL") ItemStatus status) {
        log.info("Поступил запрос от пользователя с id = {} на получение букингов со статусом = {}", userId, status);
        return bookingService.getAllUsersBookings(userId, status);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllOwnersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") ItemStatus status) {
        log.info("Поступил запрос пользователя с id = {} на получение своих букингов со статусом = {}", userId, status);
        return bookingService.getAllOwnersBookings(userId, status);
    }
}