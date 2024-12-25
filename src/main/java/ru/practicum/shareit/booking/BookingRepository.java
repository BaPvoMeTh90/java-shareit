package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByItemOwner(Long ownerId, Sort sort);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    List<Booking> findByItemOwnerAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime now);

    List<Booking> findByItemIdAndStatus(Long itemId, Status status, Sort sort);
}
