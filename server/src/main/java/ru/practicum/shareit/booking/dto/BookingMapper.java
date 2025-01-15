package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingOutputDto toBookingOutputDto(Booking booking, ItemOutputDto itemOutputDto, UserOutputDto userOutputDto) {
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(booking.getId());
        bookingOutputDto.setStart(booking.getStart());
        bookingOutputDto.setEnd(booking.getEnd());
        bookingOutputDto.setItem(itemOutputDto);
        bookingOutputDto.setBooker(userOutputDto);
        bookingOutputDto.setStatus(booking.getStatus());
        return bookingOutputDto;
    }

    public static List<BookingOutputDto> toBookingOutputDto(Iterable<Booking> bookings) {
        List<BookingOutputDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingOutputDto(booking,
                    ItemMapper.toItemOutputDto(booking.getItem()), UserMapper.toUserOutputDto(booking.getBooker())));
        }
        return result;
    }

    public static Booking toBooking(BookingInputDto bookingInputDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }
}
