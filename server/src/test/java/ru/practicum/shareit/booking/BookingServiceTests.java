package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.ItemStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BookingServiceTests {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private BookingInputDto bookingInputDto;
    private Long userId;
    private Long itemId;

    @Autowired
    private UserService userService;

    @BeforeEach
    void BeforeEach() {
        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@test.com");
        user = userRepository.save(user);
        userId = user.getId();

        Item item = new Item();
        item.setName("Test Name");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user.getId());
        item = itemRepository.save(item);
        itemId = item.getId();

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setStart(LocalDateTime.now().plusHours(1));
        bookingInputDto.setEnd(LocalDateTime.now().plusHours(2));
        bookingInputDto.setItemId(itemId);

        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(1L);
        bookingOutputDto.setStart(bookingInputDto.getStart());
        bookingOutputDto.setEnd(bookingInputDto.getEnd());
        bookingOutputDto.setItem(ItemMapper.toItemOutputDto(item));
        bookingOutputDto.setBooker(UserMapper.toUserOutputDto(user));
        bookingOutputDto.setStatus(Status.WAITING);
    }


    @Test
    void getById() {
        BookingOutputDto createdBooking = bookingService.create(bookingInputDto, userId);
        BookingOutputDto repositoryBooking = bookingService.getById(createdBooking.getId(), userId);
        assertNotNull(repositoryBooking);
        assertEquals(createdBooking.getId(), repositoryBooking.getId());
    }

    @Test
    void create() {
        BookingOutputDto createdBooking = bookingService.create(bookingInputDto, userId);
        assertNotNull(createdBooking);
        assertEquals(bookingInputDto.getStart(), createdBooking.getStart());
        assertEquals(bookingInputDto.getEnd(), createdBooking.getEnd());
        assertEquals(itemId, createdBooking.getItem().getId());
        assertEquals(userId, createdBooking.getBooker().getId());
    }

    @Test
    void updateBookingStatus() {
        BookingOutputDto createdBooking = bookingService.create(bookingInputDto, userId);
        BookingOutputDto updatedBooking = bookingService.updateBookingStatus(createdBooking.getId(), userId, true);
        assertEquals(Status.APPROVED, updatedBooking.getStatus());
    }

    @Test
    void getAllUsersBookings() {
        bookingService.create(bookingInputDto, userId);
        List<BookingOutputDto> bookings = bookingService.getAllUsersBookings(userId, ItemStatus.ALL);
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllOwnersBookings() {
        User user2 = new User();
        user2.setName("Test User2");
        user2.setEmail("test2@test.com");
        user2 = userRepository.save(user2);
        Long user2Id = user2.getId();

        BookingInputDto prevBooking = new BookingInputDto();
        prevBooking.setBookerId(userId);
        prevBooking.setItemId(itemId);
        prevBooking.setStart(LocalDateTime.now().minusDays(2));
        prevBooking.setEnd(LocalDateTime.now().minusDays(1));
        bookingService.create(prevBooking, user2Id);

        BookingInputDto currentBooking = new BookingInputDto();
        currentBooking.setBookerId(userId);
        currentBooking.setItemId(itemId);
        currentBooking.setStart(LocalDateTime.now().minusDays(1));
        currentBooking.setEnd(LocalDateTime.now().plusDays(1));
        bookingService.create(currentBooking, user2Id);

        BookingInputDto futureBooking = new BookingInputDto();
        futureBooking.setBookerId(userId);
        futureBooking.setItemId(itemId);
        futureBooking.setStart(LocalDateTime.now().plusDays(1));
        futureBooking.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(futureBooking, user2Id);

        List<BookingOutputDto> bookings = bookingService.getAllOwnersBookings(userId, ItemStatus.ALL);
        assertFalse(bookings.isEmpty());
        assertEquals(3, bookings.size());
    }
}