package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.ItemStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.DataConflict;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingOutputDto getById(Long bookingId, Long userId) {
        validateUser(userId);
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Букинга с id = " + bookingId + " не существует"));
        validateRelationForBooking(booking, userId);
        return BookingMapper.toBookingOutputDto(booking);
    }

    @Transactional
    public BookingOutputDto create(BookingInputDto bookingInputDto, Long userId) {
        User user = validateUser(userId);
        Item item = validateItem(bookingInputDto.getItemId());
//        validateItemsOwner(userId, item.getId());
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id = " + item.getId() + " не доступна к бронированию");
        }
        Booking booking = BookingMapper.toBooking(bookingInputDto, user, item);
        return BookingMapper.toBookingOutputDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingOutputDto updateBookingStatus(Long bookingId, Long userId, Boolean approve) {
        Booking booking = validateBooking(bookingId);
        validateItemsOwner(userId, booking.getItem().getId());
        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingOutputDto(booking);
    }

    public List<BookingOutputDto> getAllUsersBookings(Long userId, ItemStatus status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (status) {
            case ALL -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerId(userId, sort));
            }
            case CURRENT -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sort));
            }
            case ENDED -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), sort));
            }
            case UPCOMING -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), sort));
            }
            case WAITING -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING, sort));
            }
            case REJECTED -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED, sort));
            }
            default -> {
                return List.of();
            }
        }
    }

    public List<BookingOutputDto> getAllOwnersBookings(Long userId, ItemStatus status) {
        validateItemsAvailability(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (status) {
            case ALL -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByItemOwner(userId, sort));
            }
            case CURRENT -> {
                return BookingMapper.toBookingOutputDto(bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sort));
            }
            case ENDED -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByItemOwnerAndEndIsBefore(userId, LocalDateTime.now(), sort));
            }
            case UPCOMING -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByItemOwnerAndStartIsAfter(userId, LocalDateTime.now(), sort));
            }
            case WAITING -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByItemOwnerAndStatus(userId, Status.WAITING, sort));
            }
            case REJECTED -> {
                return BookingMapper
                        .toBookingOutputDto(bookingRepository.findByItemOwnerAndStatus(userId, Status.REJECTED, sort));
            }
            default -> {
                return List.of();
            }
        }
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с ID = " + userId + " не существует"));
    }

    private Item validateItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмета с ID = " + itemId + " не существует"));
    }

    private Booking validateBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataConflict("Букинга с id = " + bookingId + " не существует"));
    }

    private void validateItemsOwner(Long userId, Long itemId) {
        if (!itemRepository.findById(itemId).get().getOwner().equals(userId)) {
            throw new ValidationException("Пользователь c ID = " + userId + " не имеет права на предмет c ID = " + itemId);
        }
    }

    private void validateRelationForBooking(Booking booking, Long userId) {
        Long ownerId = booking.getItem().getOwner();
        if (!booking.getBooker().getId().equals(userId) && !ownerId.equals(userId)) {
            throw new DataConflict("Вы не участник букинга c ID = " + booking.getId());
        }
    }

    private void validateItemsAvailability(Long userId) {
        if (itemRepository.findByOwner(userId).isEmpty()) {
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей для букинга");
        }
    }
}